package io.github.dsh105.echopet.mysql;

import com.dsh105.dshutils.logger.Logger;
import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.data.PetHandler;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetData;
import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.util.SQLUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.*;

public class SQLPetHandler {

    public static SQLPetHandler getInstance() {
        return EchoPetPlugin.getInstance().SPH;
    }

    public void updateDatabase(String player, List<PetData> list, Boolean result, boolean isRider) {
        if (EchoPetPlugin.getInstance().options.useSql()) {
            Connection con = null;
            Statement statement = null;

            if (EchoPetPlugin.getInstance().dbPool != null) {
                try {
                    Map<String, String> updates = SQLUtil.constructUpdateMap(list, result, isRider);
                    if (!updates.isEmpty()) {
                        con = EchoPetPlugin.getInstance().dbPool.getConnection();
                        statement = con.createStatement();
                        for (Map.Entry<String, String> updateEntry : updates.entrySet()) {
                            statement.executeUpdate("UPDATE Pets SET " + updateEntry.getKey() + "='" + updateEntry.getValue() + "' WHERE OwnerName = '" + player + "'");
                        }
                    }

				/*for (PetData pd : list) {
                    PreparedStatement ps4 = con.prepareStatement("INSERT INTO Pets (OwnerName, " + s + "" + pd.toString() + ") VALUES (?, ?);");
					ps4.setString(1, player.getName());
					ps4.setString(2, b.toString());
					ps4.executeUpdate();
				}*/
                } catch (SQLException e) {
                    Logger.log(Logger.LogLevel.SEVERE, "Failed to save Pet data for " + player + " to MySQL Database", e, true);
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

    public void saveToDatabase(Pet p, boolean isRider) {
        if (EchoPetPlugin.getInstance().options.useSql()) {
            Connection con = null;
            PreparedStatement ps = null;

            if (EchoPetPlugin.getInstance().dbPool != null && p != null) {
                try {
                    con = EchoPetPlugin.getInstance().dbPool.getConnection();
                    // Delete any existing info
                    if (!isRider) {
                        this.clearFromDatabase(p.getNameOfOwner());
                    }

                    // Deal with the pet metadata first
                    // This tends to be more problematic, so by shoving it out of the way, we can get the pet data saved.
                    if (isRider)
                        ps = con.prepareStatement("INSERT INTO Pets (OwnerName, RiderPetType, RiderPetName) VALUES (?, ?, ?)");
                    else
                        ps = con.prepareStatement("INSERT INTO Pets (OwnerName, PetType, PetName) VALUES (?, ?, ?)");

                    ps.setString(1, p.getNameOfOwner());
                    ps.setString(2, p.getPetType().toString());
                    ps.setString(3, p.getPetName());
                    ps.executeUpdate();

                    this.updateDatabase(p.getNameOfOwner(), p.getPetData(), true, isRider);

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

    public Pet createPetFromDatabase(String player) {
        if (EchoPetPlugin.getInstance().options.useSql()) {
            Connection con = null;
            PreparedStatement ps = null;

            Pet pet = null;
            Player owner;
            PetType pt;
            String name;
            Map<PetData, Boolean> map = new HashMap<PetData, Boolean>();

            if (EchoPetPlugin.getInstance().dbPool != null) {
                try {
                    con = EchoPetPlugin.getInstance().dbPool.getConnection();
                    ps = con.prepareStatement("SELECT * FROM Pets WHERE OwnerName = ?;");
                    ps.setString(1, player);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        owner = Bukkit.getPlayerExact(rs.getString("OwnerName"));
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

                        PetHandler ph = PetHandler.getInstance();
                        pet = ph.createPet(owner, pt, false);
                        if (pet == null) {
                            return null;
                        }
                        pet.setPetName(name);
                        PetData[] PDT = createArray(map, true);
                        PetData[] PDF = createArray(map, false);
                        if (PDT != null) {
                            PetHandler.getInstance().setData(pet, PDT, true);
                        }
                        if (PDF != null) {
                            PetHandler.getInstance().setData(pet, PDF, false);
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

                            Pet rider = pet.createRider(mt, false);
                            if (rider != null) {
                                rider.setPetName(mName);
                                PetData[] MDT = createArray(map, true);
                                PetData[] MDF = createArray(map, false);

                                if (MDT != null) {
                                    ph.setData(rider, MDT, true);
                                }
                                if (MDF != null) {
                                    ph.setData(rider, MDF, false);
                                }
                            }
                        }
                    }
                } catch (SQLException e) {
                    Logger.log(Logger.LogLevel.SEVERE, "Failed to retrieve Pet data for " + player + " in MySQL Database", e, true);
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

    private PetData[] createArray(Map<PetData, Boolean> map, boolean b) {
        List<PetData> list = new ArrayList<PetData>();
        for (PetData pd : map.keySet()) {
            if (map.get(pd) == b) {
                list.add(pd);
            }
        }
        return list.isEmpty() ? null : list.toArray(new PetData[list.size()]);
    }

    private PetType findPetType(String s) {
        try {
            return PetType.valueOf(s.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    public void clearFromDatabase(String name) {
        if (EchoPetPlugin.getInstance().options.useSql()) {
            Connection con = null;
            PreparedStatement ps = null;

            if (EchoPetPlugin.getInstance().dbPool != null) {
                try {
                    con = EchoPetPlugin.getInstance().dbPool.getConnection();
                    ps = con.prepareStatement("DELETE FROM Pets WHERE OwnerName = ?;");
                    ps.setString(1, name);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    Logger.log(Logger.LogLevel.SEVERE, "Failed to retrieve Pet data for " + name + " in MySQL Database", e, true);
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

    public void clearRiderFromDatabase(String name) {
        if (EchoPetPlugin.getInstance().options.useSql()) {
            Connection con = null;
            PreparedStatement ps = null;

            if (EchoPetPlugin.getInstance().dbPool != null) {
                try {
                    con = EchoPetPlugin.getInstance().dbPool.getConnection();
                    String list = SQLUtil.serialiseUpdate(Arrays.asList(PetData.values()), null, true);
                    ps = con.prepareStatement("UPDATE Pets SET ? WHERE OwnerName = ?;");
                    ps.setString(1, list);
                    ps.setString(2, name);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    Logger.log(Logger.LogLevel.SEVERE, "Failed to retrieve Pet data for " + name + " in MySQL Database", e, true);
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