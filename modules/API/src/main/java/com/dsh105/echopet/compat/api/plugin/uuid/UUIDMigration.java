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
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class UUIDMigration {

    public static boolean canReturnUUID() {
        try {
            Bukkit.class.getDeclaredMethod("getPlayer", UUID.class);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public static Object getIdentificationFor(Player player) {
        if (ReflectionUtil.MC_VERSION_NUMERIC >= 172 && canReturnUUID()) {
            return player.getUniqueId();
        } else {
            return player.getName();
        }
    }

    public static String getIdentificationForAsString(Player player) {
        if (ReflectionUtil.MC_VERSION_NUMERIC >= 172 && canReturnUUID()) {
            return player.getUniqueId().toString();
        } else {
            return player.getName();
        }
    }

    public static Player getPlayerOf(Object identification) {
        if (ReflectionUtil.MC_VERSION_NUMERIC >= 172 && canReturnUUID()) {
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
}
