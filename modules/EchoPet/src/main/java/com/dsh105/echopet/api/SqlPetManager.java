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

package com.dsh105.echopet.api;

import com.dsh105.dshutils.logger.Logger;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.plugin.ISqlPetManager;
import com.dsh105.echopet.compat.api.util.SQLUtil;
import com.dsh105.echopet.compat.api.plugin.uuid.SaveConversion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlPetManager implements ISqlPetManager {

    @Override
    public void updateDatabase(Player player, List<PetData> list, Boolean result, boolean isRider) {
        if (EchoPet.getOptions().useSql()) {
            Connection con = null;
            Statement statement = null;

            if (EchoPet.getPlugin().getDbPool() != null) {
                try {
                    Map<String, String> updates = SQLUtil.constructUpdateMap(list, result, isRider);
                    if (!updates.isEmpty()) {
                        con = EchoPet.getPlugin().getDbPool().getConnection();
                        statement = con.createStatement();
                        for (Map.Entry<String, String> updateEntry : updates.entrySet()) {
                            statement.executeUpdate("UPDATE Pets SET " + updateEntry.getKey() + "='" + updateEntry.getValue() + "' WHERE Owner = '" + SaveConversion.getSavePath(player) + "'");
                        }
                    }
                } catch (SQLException e) {
                    Logger.log(Logger.LogLevel.SEVERE, "Failed to save Pet data for " + player.getName() + " to MySQL Database", e, true);
                } finally {
                    try {
                        if (statement != null)
                            statement.close();
                        if (con != null)
                            con.close();
                    } catch (SQLException ignored) {
                    }
                }
            }
        }
    }

    @Override
    public void saveToDatabase(IPet p, boolean isRider) {
        if (EchoPet.getOptions().useSql()) {
            Connection con = null;
            PreparedStatement ps = null;

            if (EchoPet.getPlugin().getDbPool() != null && p != null) {
                try {
                    con = EchoPet.getPlugin().getDbPool().getConnection();
                    // Delete any existing info
                    if (!isRider) {
                        this.clearFromDatabase(p.getOwner());
                    }

                    // Deal with the pet metadata first
                    // This tends to be more problematic, so by shoving it out of the way, we can get the pet data saved.
                    if (isRider)
                        ps = con.prepareStatement("INSERT INTO Pets (Owner, RiderPetType, RiderPetName) VALUES (?, ?, ?)");
                    else
                        ps = con.prepareStatement("INSERT INTO Pets (Owner, PetType, PetName) VALUES (?, ?, ?)");

                    ps.setString(1, SaveConversion.getSavePath(p.getOwner()));
                    ps.setString(2, p.getPetType().toString());
                    ps.setString(3, p.getPetName());
                    ps.executeUpdate();

                    this.updateDatabase(p.getOwner(), p.getPetData(), true, isRider);

                    this.saveToDatabase(p.getRider(), true);

                } catch (SQLException e) {
                    Logger.log(Logger.LogLevel.SEVERE, "Failed to save Pet data for " + p.getNameOfOwner() + " to MySQL Database", e, true);
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
    }

    @Override
    public IPet createPetFromDatabase(Player player) {
        if (EchoPet.getOptions().useSql()) {
            Connection con = null;
            PreparedStatement ps = null;

            IPet pet = null;
            Player owner;
            PetType pt;
            String name;
            Map<PetData, Boolean> map = new HashMap<PetData, Boolean>();

            if (EchoPet.getPlugin().getDbPool() != null) {
                try {
                    con = EchoPet.getPlugin().getDbPool().getConnection();
                    ps = con.prepareStatement("SELECT * FROM Pets WHERE Owner = ?;");
                    ps.setString(1, SaveConversion.getSavePath(player));
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        owner = Bukkit.getPlayerExact(rs.getString("Owner"));
                        pt = findPetType(rs.getString("PetType"));
                        if (pt == null) {
                            return null;
                        }
                        name = rs.getString("PetName").replace("\'", "'");

                        for (PetData pd : PetData.values()) {
                            if (rs.getString(pd.toString()) != null) {
                                map.put(pd, Boolean.valueOf(rs.getString(pd.toString())));
                            }
                        }

                        if (owner == null) {
                            return null;
                        }

                        pet = EchoPet.getManager().createPet(owner, pt, false);
                        if (pet == null) {
                            return null;
                        }
                        pet.setPetName(name);
                        for (Map.Entry<PetData, Boolean> entry : map.entrySet()) {
                            EchoPet.getManager().setData(pet, entry.getKey(), entry.getValue());
                        }
                        if (rs.getString("RiderPetType") != null) {
                            PetType mt = findPetType(rs.getString("RiderPetType"));
                            if (mt == null) {
                                return null;
                            }
                            String mName = rs.getString("RiderPetName").replace("\'", "'");
                            for (PetData pd : PetData.values()) {
                                if (rs.getString("Rider" + pd.toString()) != null) {
                                    map.put(pd, Boolean.valueOf(rs.getString("Rider" + pd.toString())));
                                }
                            }

                            IPet rider = pet.createRider(mt, false);
                            if (rider != null) {
                                rider.setPetName(mName);
                                for (Map.Entry<PetData, Boolean> entry : map.entrySet()) {
                                    EchoPet.getManager().setData(rider, entry.getKey(), entry.getValue());
                                }
                            }
                        }
                    }
                } catch (SQLException e) {
                    Logger.log(Logger.LogLevel.SEVERE, "Failed to retrieve Pet data for " + player.getName() + " in MySQL Database", e, true);
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


            return pet;
        }
        return null;
    }

    private PetType findPetType(String s) {
        try {
            return PetType.valueOf(s.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void clearFromDatabase(Player player) {
        if (EchoPet.getOptions().useSql()) {
            Connection con = null;
            PreparedStatement ps = null;

            if (EchoPet.getPlugin().getDbPool() != null) {
                try {
                    con = EchoPet.getPlugin().getDbPool().getConnection();
                    ps = con.prepareStatement("DELETE FROM Pets WHERE Owner = ?;");
                    ps.setString(1, SaveConversion.getSavePath(player));
                    ps.executeUpdate();
                } catch (SQLException e) {
                    Logger.log(Logger.LogLevel.SEVERE, "Failed to retrieve Pet data for " + player.getName() + " in MySQL Database", e, true);
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
    }

    @Override
    public void clearRiderFromDatabase(Player player) {
        if (EchoPet.getOptions().useSql()) {
            Connection con = null;
            PreparedStatement ps = null;

            if (EchoPet.getPlugin().getDbPool() != null) {
                try {
                    con = EchoPet.getPlugin().getDbPool().getConnection();
                    String list = SQLUtil.serialiseUpdate(Arrays.asList(PetData.values()), true);
                    ps = con.prepareStatement("UPDATE Pets SET ? WHERE Owner = ?;");
                    ps.setString(1, list);
                    ps.setString(2, SaveConversion.getSavePath(player));
                    ps.executeUpdate();
                } catch (SQLException e) {
                    Logger.log(Logger.LogLevel.SEVERE, "Failed to retrieve Pet data for " + player.getName() + " in MySQL Database", e, true);
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
    }
}