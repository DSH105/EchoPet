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

import com.dsh105.commodus.PlayerIdent;
import com.dsh105.echopet.compat.api.config.Settings;
import com.dsh105.echopet.compat.api.entity.pet.Pet;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.plugin.ISqlPetManager;
import com.dsh105.echopet.compat.api.util.SQLUtil;
import com.dsh105.echopet.compat.api.util.TableMigrationUtil;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class SqlPetManager implements ISqlPetManager {

    @Override
    public void saveToDatabase(Pet p, boolean isRider) {
        this.saveToDatabase(p.getOwnerIdentification().toString(), p.getPetType(), p.getPetName(), p.getActiveData(), false);
        if (p.getRider() != null) {
            this.saveToDatabase(p.getOwnerIdentification().toString(), p.getRider().getPetType(), p.getRider().getPetName(), p.getRider().getActiveData(), true);
        }
    }

    @Override
    public void saveToDatabase(String playerIdent, PetType petType, String petName, List<PetData> petData, boolean isRider) {
        if (Settings.SQL_ENABLE.getValue()) {
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
                    if (isRider) {
                        ps = con.prepareStatement("UPDATE " + TableMigrationUtil.LATEST_TABLE + " SET RiderPetType = ?, RiderPetName = ?, RiderPetData = ? WHERE OwnerName = ?");

                        ps.setString(1, petType.storageName());
                        ps.setString(2, petName);
                        ps.setLong(3, SQLUtil.serializePetData(petData));
                        ps.setString(4, String.valueOf(playerIdent));
                        ps.executeUpdate();
                    } else {
                        ps = con.prepareStatement("INSERT INTO " + TableMigrationUtil.LATEST_TABLE + " (OwnerName, PetType, PetName, PetData) VALUES (?, ?, ?, ?)");

                        ps.setString(1, String.valueOf(playerIdent));
                        ps.setString(2, petType.storageName());
                        ps.setString(3, petName);
                        ps.setLong(4, SQLUtil.serializePetData(petData));
                        ps.executeUpdate();
                    }

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
    public Pet createPetFromDatabase(Player player) {
        return this.createPetFromDatabase(PlayerIdent.getIdentificationForAsString(player));
    }

    @Override
    public Pet createPetFromDatabase(String playerIdent) {
        if (Settings.SQL_ENABLE.getValue()) {
            Connection con = null;
            PreparedStatement ps = null;

            Pet pet = null;
            Player owner;
            PetType pt;
            String name;

            if (EchoPet.getPlugin().getDbPool() != null) {
                try {
                    con = EchoPet.getPlugin().getDbPool().getConnection();
                    ps = con.prepareStatement("SELECT * FROM " + TableMigrationUtil.LATEST_TABLE + " WHERE OwnerName = ?;");
                    ps.setString(1, String.valueOf(playerIdent));
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        owner = PlayerIdent.getPlayerOf(rs.getString("OwnerName"));
                        if (owner == null) {
                            return null;
                        }

                        pt = findPetType(rs.getString("PetType"));
                        if (pt == null) {
                            return null;
                        }
                        name = rs.getString("PetName").replace("\'", "'");

                        List<PetData> dataList = SQLUtil.deserializePetData(rs.getLong("PetData"));

                        pet = EchoPet.getManager().createPet(owner, pt, false);
                        if (pet == null) {
                            return null;
                        }
                        pet.setPetName(name);
                        for (PetData data : dataList) {
                            pet.setDataValue(data);
                        }
                        if (rs.getString("RiderPetType") != null) {
                            PetType mt = findPetType(rs.getString("RiderPetType"));
                            if (mt == null) {
                                return null;
                            }
                            String mName = rs.getString("RiderPetName").replace("\'", "'");

                            List<PetData> riderDataList = SQLUtil.deserializePetData(rs.getLong("RiderPetData"));

                            Pet rider = pet.createRider(mt, false);
                            if (rider != null) {
                                rider.setPetName(mName);
                                for (PetData data : riderDataList) {
                                    rider.setDataValue(data);
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
        this.clearFromDatabase(PlayerIdent.getIdentificationForAsString(player));
    }

    @Override
    public void clearFromDatabase(String playerIdent) {
        if (Settings.SQL_ENABLE.getValue()) {
            Connection con = null;
            PreparedStatement ps = null;

            if (EchoPet.getPlugin().getDbPool() != null) {
                try {
                    con = EchoPet.getPlugin().getDbPool().getConnection();
                    ps = con.prepareStatement("DELETE FROM " + TableMigrationUtil.LATEST_TABLE + " WHERE OwnerName = ?;");
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
        this.clearRiderFromDatabase(PlayerIdent.getIdentificationForAsString(player));
    }

    @Override
    public void clearRiderFromDatabase(String playerIdent) {
        if (Settings.SQL_ENABLE.getValue()) {
            Connection con = null;
            PreparedStatement ps = null;

            if (EchoPet.getPlugin().getDbPool() != null) {
                try {
                    con = EchoPet.getPlugin().getDbPool().getConnection();
                    ps = con.prepareStatement("UPDATE " + TableMigrationUtil.LATEST_TABLE + " SET RiderData = ? WHERE OwnerName = ?;");
                    ps.setLong(1, SQLUtil.serializePetData(Arrays.asList(PetData.values())));
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
