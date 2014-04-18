/*
 * This file is part of EchoPet.
 *
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.compat.api.plugin.uuid;

import com.dsh105.dshutils.config.YAMLConfig;
import com.dsh105.dshutils.logger.Logger;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.plugin.ISqlPetManager;
import com.dsh105.echopet.compat.api.util.ReflectionUtil;
import com.dsh105.echopet.compat.api.util.SQLUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.*;

public class UUIDMigration {

    public static Object getIdentificationFor(Player player) {
        if (ReflectionUtil.MC_VERSION_NUMERIC >= 172) {
            return player.getUniqueId();
        } else {
            return player.getName();
        }
    }

    public static String getIdentificationForAsString(Player player) {
        if (ReflectionUtil.MC_VERSION_NUMERIC >= 172) {
            return player.getUniqueId().toString();
        } else {
            return player.getName();
        }
    }

    public static Player getPlayerOf(Object identification) {
        if (ReflectionUtil.MC_VERSION_NUMERIC >= 172) {
            if (identification instanceof UUID) {
                return Bukkit.getPlayer((UUID) identification);
            } else if (identification instanceof String) {
                return Bukkit.getPlayer(UUID.fromString((String) identification));
            }
        } else if (identification instanceof String) {
            return Bukkit.getPlayerExact((String) identification);
        }
        return null;
    }

    public static void migrateConfig(final YAMLConfig config) {
        ConfigurationSection cs = config.getConfigurationSection("autosave");
        if (cs != null) {
            final LinkedHashMap<String, LinkedHashMap<String, Object>> keyToValueMap = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
            for (String key : cs.getKeys(false)) {
                try {
                    UUID.fromString(key);
                    continue;
                } catch (IllegalArgumentException e) {
                    // Do nothing and keep migrating
                }
                ConfigurationSection petSection = config.getConfigurationSection("autosave." + key);
                for (String fullPath : petSection.getKeys(true)) {
                    LinkedHashMap<String, Object> existingMap = keyToValueMap.get(key);
                    if (existingMap == null) {
                        existingMap = new LinkedHashMap<String, Object>();
                    }
                    existingMap.put(fullPath, petSection.get(fullPath));
                    keyToValueMap.put(key, existingMap);
                    config.set("autosave." + key, null);
                }
            }


            new BukkitRunnable() {

                @Override
                public void run() {
                    try {
                        final Map<String, UUID> uuidMap = new UUIDFetcher(new ArrayList<String>(keyToValueMap.keySet())).call();

                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                for (Map.Entry<String, UUID> entry : uuidMap.entrySet()) {
                                    for (Map.Entry<String, Object> valueEntries : keyToValueMap.get(entry.getKey()).entrySet()) {
                                        config.set("autosave." + entry.getValue() + "." + valueEntries.getKey(), valueEntries.getValue());
                                    }
                                }
                                config.saveConfig();
                            }
                        }.runTask(EchoPet.getPlugin());

                    } catch (Exception e) {
                    }
                }

            }.runTaskAsynchronously(EchoPet.getPlugin());
        }
    }

    public static void migrateSqlTable() {
        Connection con = null;
        PreparedStatement ps = null;

        if (EchoPet.getPlugin().getDbPool() != null) {
            try {
                ps = con.prepareStatement("SELECT * FROM Pets;");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String ownerName = rs.getString("Owner");
                    if (ownerName == null) {
                        continue;
                    }

                    // Check if it already is a UUID
                    try {
                        UUID.fromString(ownerName);
                        continue;
                    } catch (IllegalArgumentException e) {
                        // Do nothing and keep migrating
                    }

                    UUID playerUuid;
                    try {
                        playerUuid = UUIDFetcher.getUUIDOf(ownerName);
                    } catch (Exception e) {
                        continue;
                    }
                    if (playerUuid == null) {
                        continue;
                    }

                    PetType pt = findPetType(rs.getString("PetType"));
                    if (pt == null) {
                        continue;
                    }
                    String petName = rs.getString("PetName").replace("\'", "'");

                    List<PetData> petData = new ArrayList<PetData>();
                    for (PetData pd : PetData.values()) {
                        if (rs.getString(pd.toString()) != null) {
                            if (Boolean.valueOf(rs.getString(pd.toString()))) {
                                petData.add(pd);
                            }
                        }
                    }

                    EchoPet.getSqlManager().saveToDatabase(playerUuid.toString(), pt, petName, petData, false);

                    if (rs.getString("RiderPetType") != null) {
                        PetType mt = findPetType(rs.getString("RiderPetType"));
                        if (mt == null) {
                            continue;
                        }
                        String mName = rs.getString("RiderPetName").replace("\'", "'");

                        List<PetData> mountData = new ArrayList<PetData>();
                        for (PetData pd : PetData.values()) {
                            if (rs.getString("Rider" + pd.toString()) != null) {
                                if (Boolean.valueOf(rs.getString("Rider" + pd.toString()))) {
                                    mountData.add(pd);
                                }
                            }
                        }
                        EchoPet.getSqlManager().saveToDatabase(playerUuid.toString(), mt, mName, mountData, true);
                    }

                }
            } catch (SQLException e) {
                Logger.log(Logger.LogLevel.SEVERE, "Failed to migrate SQL database", e, true);
            } finally {
                try {
                    if (ps != null)
                        ps.close();
                    if (con != null)
                        con.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    private static PetType findPetType(String s) {
        try {
            return PetType.valueOf(s.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}
