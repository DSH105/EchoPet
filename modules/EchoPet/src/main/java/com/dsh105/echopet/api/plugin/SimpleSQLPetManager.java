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

package com.dsh105.echopet.api.plugin;

import com.dsh105.commodus.IdentUtil;
import com.dsh105.echopet.api.entity.AttributeAccessor;
import com.dsh105.echopet.api.entity.PetData;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.util.SQLUtil;
import com.dsh105.echopet.util.TableMigrationUtil;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class SimpleSQLPetManager extends SimplePetManager implements SQLPetManager {

    @Override
    public void save(Pet pet) {
        super.save(pet);

        save(pet, false);
    }

    @Override
    public void clear(String playerIdent) {
        Connection con = null;
        PreparedStatement ps = null;

        if (EchoPet.getCore().getDbPool() != null) {
            try {
                con = EchoPet.getCore().getDbPool().getConnection();
                ps = con.prepareStatement("DELETE FROM " + TableMigrationUtil.LATEST_TABLE + " WHERE OwnerName = ?");
                ps.setString(1, String.valueOf(playerIdent));
                ps.executeUpdate();
            } catch (SQLException e) {
                EchoPet.LOG.severe("Failed to save Pet data for " + playerIdent + " to MySQL Database");
                e.printStackTrace();
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public List<Pet> load(Player player, boolean sendMessage) {
        List<Pet> loadedPets = loadPets(player);
        if (loadedPets == null) {
            return super.load(player, sendMessage);
        }
        return loadedPets;
    }


    @Override
    public void save(Pet pet, boolean isRider) {
        save(pet.getPetId().toString(), pet.getOwnerIdent(), pet.getType(), pet.getName(), AttributeAccessor.getActiveDataValues(pet), false);
        if (pet.getRider() != null) {
            save(pet.getRider(), true);
        }
    }

    @Override
    public void save(String petUniqueId, String playerIdent, PetType petType, String petName, List<PetData> petData, boolean isRider) {
        Connection con = null;
        PreparedStatement ps = null;

        if (EchoPet.getCore().getDbPool() != null) {
            try {
                con = EchoPet.getCore().getDbPool().getConnection();
                // Delete any existing info
                if (!isRider) {
                    clear(playerIdent);
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
                EchoPet.LOG.severe("Failed to save Pet data for " + playerIdent + " to MySQL Database");
                e.printStackTrace();
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public List<Pet> loadPets(Player player) {
        return load(IdentUtil.getIdentificationForAsString(player));
    }

    @Override
    public List<Pet> load(String playerIdent) {
        Connection con = null;
        PreparedStatement ps = null;

        Pet pet;
        Player owner;
        PetType petType;
        String name;

        if (EchoPet.getCore().getDbPool() != null) {
            try {
                con = EchoPet.getCore().getDbPool().getConnection();
                ps = con.prepareStatement("SELECT * FROM " + TableMigrationUtil.LATEST_TABLE + " WHERE OwnerName = ?;");
                ps.setString(1, String.valueOf(playerIdent));
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    owner = IdentUtil.getPlayerOf(rs.getString("OwnerName"));
                    if (owner == null) {
                        return null;
                    }
                    try {
                        petType = PetType.valueOf(rs.getString("PetType"));
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                    name = rs.getString("PetName").replace("\'", "'");
                    List<PetData> dataList = SQLUtil.deserializePetData(rs.getLong("PetData"));

                    pet = super.create(owner, petType, true);
                    if (pet == null) {
                        return null;
                    }

                    pet.setName(name);
                    for (PetData petData : dataList) {
                        pet.setDataValue(petData);
                    }

                    if (rs.getString("RiderPetType") != null) {
                        PetType riderType;
                        try {
                            riderType = PetType.valueOf(rs.getString("PetType"));
                        } catch (IllegalArgumentException e) {
                            return null;
                        }
                        String riderName = rs.getString("RiderPetName").replace("\'", "'");
                        List<PetData> riderDataList = SQLUtil.deserializePetData(rs.getLong("RiderPetData"));

                        Pet rider = pet.spawnRider(riderType, true);
                        if (rider != null) {
                            rider.setName(riderName);
                            for (PetData petData : riderDataList) {
                                rider.setDataValue(petData);
                            }
                        }
                    }
                }

            } catch (SQLException e) {
                EchoPet.LOG.severe("Failed to retrieve Pet data for " + playerIdent + " in MySQL Database");
                e.printStackTrace();
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException ignored) {
                }
            }
        }
        return null;
    }

    @Override
    public Pet load(String petUniqueId, String playerIdent) {
        return null;
    }

    @Override
    public void clearRider(Pet pet) {
        clearRider(pet.getPetId().toString(), pet.getOwnerIdent());
    }

    @Override
    public void clearRider(String petUniqueId, Player player) {
        clearRider(petUniqueId, IdentUtil.getIdentificationForAsString(player));
    }

    @Override
    public void clearRider(String petUniqueId, String playerIdent) {
        Connection con = null;
        PreparedStatement ps = null;

        if (EchoPet.getCore().getDbPool() != null) {
            try {
                con = EchoPet.getCore().getDbPool().getConnection();
                ps = con.prepareStatement("UPDATE " + TableMigrationUtil.LATEST_TABLE + " SET RiderData = ? WHERE OwnerName = ?;");
                ps.setLong(1, SQLUtil.serializePetData(Arrays.asList(PetData.values())));
                ps.setString(2, String.valueOf(playerIdent));
                ps.executeUpdate();
            } catch (SQLException e) {
                EchoPet.LOG.severe("Failed to save Pet data for " + playerIdent + " to MySQL Database");
                e.printStackTrace();
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException ignored) {
                }
            }
        }
    }
}