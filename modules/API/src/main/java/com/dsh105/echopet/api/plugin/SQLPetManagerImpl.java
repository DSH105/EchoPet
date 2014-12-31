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

import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.attribute.EntityAttribute;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.bridge.Ident;
import com.dsh105.echopet.bridge.SchedulerBridge;
import com.dsh105.echopet.util.SQLUtil;
import com.dsh105.echopet.util.TableMigrationUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLPetManagerImpl extends PetManagerImpl implements SQLPetManager {

    @Override
    public void save(Pet pet) {
        // Save to file as well
        super.save(pet);

        save(pet, false);
    }

    @Override
    public void clear(final UUID playerUID) {
        if (EchoPet.getNucleus().getDbPool() != null) {
            EchoPet.getBridge(SchedulerBridge.class).run(true, new Runnable() {
                @Override
                public void run() {
                    Connection con = null;
                    PreparedStatement ps = null;

                    try {
                        con = EchoPet.getNucleus().getDbPool().getConnection();
                        ps = con.prepareStatement("DELETE FROM " + TableMigrationUtil.LATEST_TABLE + " WHERE OwnerId = ?");
                        ps.setString(1, String.valueOf(playerUID));
                        ps.executeUpdate();
                    } catch (SQLException e) {
                        EchoPet.log().severe("Failed to save Pet data for " + playerUID + " to MySQL Database");
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
            });
        }
    }

    @Override
    public void save(Pet pet, boolean isRider) {
        save(pet.getPetId(), pet.getOwnerUID(), pet.getType(), pet.getName(), ((Pet<?, ?>) pet).getActiveAttributes(), false);
        if (pet.getRider() != null) {
            save(pet.getRider(), true);
        }
    }


    @Override
    public void save(final UUID petUniqueId, final UUID playerUID, final PetType petType, final String petName, final List<EntityAttribute> entityAttributes, final boolean isRider) {
        if (EchoPet.getNucleus().getDbPool() != null) {
            EchoPet.getBridge(SchedulerBridge.class).run(true, new Runnable() {
                @Override
                public void run() {
                    Connection con = null;
                    PreparedStatement ps = null;

                    try {
                        con = EchoPet.getNucleus().getDbPool().getConnection();
                        // Delete any existing info
                        if (!isRider) {
                            clear(playerUID);
                        }

                        // Deal with the pet metadata first
                        // This tends to be more problematic, so by shoving it out of the way, we can get the pet data saved.
                        if (isRider) {
                            ps = con.prepareStatement("UPDATE " + TableMigrationUtil.LATEST_TABLE + " SET RiderType = ?, RiderName = ?, RiderAttributes = ? WHERE OwnerId = ?");

                            ps.setString(1, petType.storageName());
                            ps.setString(2, petName);
                            ps.setLong(3, SQLUtil.serializeAttributes(entityAttributes));
                            ps.setString(4, String.valueOf(playerUID));
                            ps.executeUpdate();
                        } else {
                            ps = con.prepareStatement("INSERT INTO " + TableMigrationUtil.LATEST_TABLE + " (PetId, OwnerId, PetType, PetName, Attributes) VALUES (?, ?, ?, ?, ?)");

                            ps.setString(1, String.valueOf(petUniqueId));
                            ps.setString(2, String.valueOf(playerUID));
                            ps.setString(3, petType.storageName());
                            ps.setString(4, petName);
                            ps.setLong(5, SQLUtil.serializeAttributes(entityAttributes));
                            ps.executeUpdate();
                        }

                    } catch (SQLException e) {
                        EchoPet.log().severe("Failed to save Pet data for " + playerUID + " to MySQL Database");
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
            });
        }
    }

    @Override
    public void load(final UUID playerUID, final boolean sendMessage) {
        loadPets(playerUID, new LoadCallback<List<Pet>>() {
            @Override
            public void call(List<Pet> loadedPets) {
                if (loadedPets == null) {
                    SQLPetManagerImpl.super.load(playerUID, sendMessage);
                }
            }
        });
    }

    @Override
    public void load(final UUID playerUID, final boolean sendMessage, final LoadCallback<List<Pet>> callback) {
        loadPets(playerUID, new LoadCallback<List<Pet>>() {
            @Override
            public void call(List<Pet> loadedPets) {
                if (loadedPets == null) {
                    SQLPetManagerImpl.super.load(playerUID, sendMessage, callback);
                } else {
                    if (callback != null) {
                        callback.call(loadedPets);
                    }
                }
            }
        });
    }

    @Override
    public void loadPets(final UUID playerUID) {
        loadPets(playerUID, null);
    }

    @Override
    public void loadPets(final UUID playerUID, final LoadCallback<List<Pet>> callback) {
        if (EchoPet.getNucleus().getDbPool() != null) {
            EchoPet.getBridge(SchedulerBridge.class).run(true, new Runnable() {
                @Override
                public void run() {
                    List<Pet> loadedPets = new ArrayList<>();

                    Connection con = null;
                    PreparedStatement ps = null;

                    try {
                        con = EchoPet.getNucleus().getDbPool().getConnection();
                        ps = con.prepareStatement("SELECT * FROM " + TableMigrationUtil.LATEST_TABLE + " WHERE OwnerId = ?;");
                        ps.setString(1, String.valueOf(playerUID));
                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {
                            PetType petType;
                            UUID ownerUID = StringUtil.convertUUID(rs.getString("OwnerId"));
                            try {
                                petType = PetType.valueOf(rs.getString("PetType").toUpperCase());
                            } catch (IllegalArgumentException e) {
                                continue;
                            }
                            String name = rs.getString("PetName").replace("\'", "'");
                            List<EntityAttribute> attributeList = SQLUtil.deserializeAttributes(rs.getLong("Attributes"));

                            Pet pet = SQLPetManagerImpl.super.create(ownerUID, petType, true);
                            if (pet == null) {
                                continue;
                            }

                            pet.setName(name);
                            for (EntityAttribute entityAttribute : attributeList) {
                                pet.setAttribute(entityAttribute);
                            }

                            if (rs.getString("RiderPetType") != null) {
                                PetType riderType = null;
                                try {
                                    riderType = PetType.valueOf(rs.getString("PetType").toUpperCase());
                                } catch (IllegalArgumentException ignored) {
                                }
                                if (riderType != null) {
                                    String riderName = rs.getString("RiderPetName").replace("\'", "'");
                                    List<EntityAttribute> riderAttributes = SQLUtil.deserializeAttributes(rs.getLong("RiderAttributes"));

                                    Pet rider = pet.spawnRider(riderType, true);
                                    if (rider != null) {
                                        rider.setName(riderName);
                                        for (EntityAttribute entityAttribute : riderAttributes) {
                                            rider.setAttribute(entityAttribute);
                                        }
                                    }
                                }
                            }

                            loadedPets.add(pet);
                        }


                        if (callback != null) {
                            callback.call(loadedPets);
                        }

                    } catch (SQLException e) {
                        EchoPet.log().severe("Failed to retrieve Pet data for " + playerUID + " in MySQL Database");
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
            });
        }
    }

    @Override
    public void loadPet(UUID playerUID, UUID petUniqueId, LoadCallback<Pet> callback) {
        // TODO
    }

    @Override
    public void clearRider(Pet pet) {
        clearRider(pet.getPetId());
    }

    @Override
    public void clearRider(final UUID petUniqueId) {
        if (EchoPet.getNucleus().getDbPool() != null) {

            EchoPet.getBridge(SchedulerBridge.class).run(true, new Runnable() {
                @Override
                public void run() {
                    Connection con = null;
                    PreparedStatement ps = null;

                    try {
                        con = EchoPet.getNucleus().getDbPool().getConnection();
                        ps = con.prepareStatement("UPDATE " + TableMigrationUtil.LATEST_TABLE + " SET SET RiderType = ?, RiderName = ?, RiderAttributes = ? WHERE PetId = ?;");
                        ps.setString(1, null);
                        ps.setString(2, null);
                        ps.setLong(3, 0);
                        ps.setString(4, petUniqueId.toString());
                        ps.executeUpdate();
                    } catch (SQLException e) {
                        EchoPet.log().severe("Failed to save Pet data (" + petUniqueId + ") to MySQL Database");
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
            });
        }
    }
}