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
import com.dsh105.echopet.compat.api.plugin.uuid.UUIDMigration;
import com.dsh105.echopet.compat.api.util.SQLUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.*;

public class SqlPetManager implements ISqlPetManager {

    @Override
    public void updateDatabase(Player player, List<PetData> list, Boolean result, boolean isRider) {
        this.updateDatabase(UUIDMigration.getIdentificationForAsString(player), list, result, isRider);
    }

    @Override
    public void updateDatabase(String playerIdent, List<PetData> list, Boolean result, boolean isRider) {
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
                            statement.executeUpdate("UPDATE Pets SET " + updateEntry.getKey() + "='" + updateEntry.getValue() + "' WHERE Owner = '" + playerIdent + "'");
                        }
                    }
                } catch (SQLException e) {
                    Logger.log(Logger.LogLevel.SEVERE, "Failed to save Pet data for " + playerIdent + " to MySQL Database", e, true);
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
        this.saveToDatabase(p.getOwnerIdentification().toString(), p.getPetType(), p.getPetName(), p.getPetData(), false);
        this.saveToDatabase(p.getOwnerIdentification().toString(), p.getRider().getPetType(), p.getRider().getPetName(), p.getRider().getPetData(), true);
    }

    @Override
    public void saveToDatabase(String playerIdent, PetType petType, String petName, List<PetData> petData, boolean isRider) {
        if (EchoPet.getOptions().useSql()) {
            Connection con = null;
            PreparedStatement ps = null;

            if (EchoPet.getPlugin().getDbPool() != null) {
                try {
                    con = EchoPet.getPlugin().getDbPool().getConnection();
                    // Delete any existing info
                    if (!isRider) {
                        this.clearFromDatabase(playerIdent);
                    }

                    // Deal with the pet metadata first
                    // This tends to be more problematic, so by shoving it out of the way, we can get the pet data saved.
                    if (isRider)
                        ps = con.prepareStatement("INSERT INTO Pets (Owner, RiderPetType, RiderPetName) VALUES (?, ?, ?)");
                    else
                        ps = con.prepareStatement("INSERT INTO Pets (Owner, PetType, PetName) VALUES (?, ?, ?)");

                    ps.setString(1, String.valueOf(playerIdent));
                    ps.setString(2, petType.toString());
                    ps.setString(3, petName);
                    ps.executeUpdate();

                    this.updateDatabase(playerIdent, petData, true, isRider);

                    //this.saveToDatabase(playerIdent, true);

                } catch (SQLException e) {
                    Logger.log(Logger.LogLevel.SEVERE, "Failed to save Pet data for " + playerIdent + " to MySQL Database", e, true);
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
        return this.createPetFromDatabase(UUIDMigration.getIdentificationForAsString(player));
    }

    @Override
    public IPet createPetFromDatabase(String playerIdent) {
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
                    ps.setString(1, String.valueOf(playerIdent));
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        owner = UUIDMigration.getPlayerOf(rs.getString("Owner"));
                        if (owner == null) {
                            return null;
                        }

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
                    Logger.log(Logger.LogLevel.SEVERE, "Failed to retrieve Pet data for " + playerIdent + " in MySQL Database", e, true);
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
        this.clearFromDatabase(UUIDMigration.getIdentificationForAsString(player));
    }

    @Override
    public void clearFromDatabase(String playerIdent) {
        if (EchoPet.getOptions().useSql()) {
            Connection con = null;
            PreparedStatement ps = null;

            if (EchoPet.getPlugin().getDbPool() != null) {
                try {
                    con = EchoPet.getPlugin().getDbPool().getConnection();
                    ps = con.prepareStatement("DELETE FROM Pets WHERE Owner = ?;");
                    ps.setString(1, String.valueOf(playerIdent));
                    ps.executeUpdate();
                } catch (SQLException e) {
                    Logger.log(Logger.LogLevel.SEVERE, "Failed to retrieve Pet data for " + playerIdent + " in MySQL Database", e, true);
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
        this.clearRiderFromDatabase(UUIDMigration.getIdentificationForAsString(player));
    }

    @Override
    public void clearRiderFromDatabase(String playerIdent) {
        if (EchoPet.getOptions().useSql()) {
            Connection con = null;
            PreparedStatement ps = null;

            if (EchoPet.getPlugin().getDbPool() != null) {
                try {
                    con = EchoPet.getPlugin().getDbPool().getConnection();
                    String list = SQLUtil.serialiseUpdate(Arrays.asList(PetData.values()), true);
                    ps = con.prepareStatement("UPDATE Pets SET ? WHERE Owner = ?;");
                    ps.setString(1, list);
                    ps.setString(2, String.valueOf(playerIdent));
                    ps.executeUpdate();
                } catch (SQLException e) {
                    Logger.log(Logger.LogLevel.SEVERE, "Failed to retrieve Pet data for " + playerIdent + " in MySQL Database", e, true);
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
