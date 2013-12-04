package io.github.dsh105.echopet.mysql;

import io.github.dsh105.echopet.EchoPet;
import io.github.dsh105.echopet.entity.living.data.PetData;
import io.github.dsh105.echopet.data.PetHandler;
import io.github.dsh105.echopet.entity.living.data.PetType;
import io.github.dsh105.echopet.entity.living.LivingPet;
import io.github.dsh105.echopet.logger.Logger;
import io.github.dsh105.echopet.util.SQLUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLPetHandler {

    public static SQLPetHandler getInstance() {
        return EchoPet.getInstance().SPH;
    }

    public void updateDatabase(Player player, ArrayList<PetData> list, Boolean result, boolean isMount) {
        if (EchoPet.getInstance().options.useSql()) {
            Connection con = EchoPet.getInstance().getSqlCon();

            if (con != null) {
                try {
                    String data = SQLUtil.serialiseUpdate(list, result, isMount);
                    if (!data.equalsIgnoreCase("")) {
                        PreparedStatement ps = con.prepareStatement("UPDATE Pets SET ? WHERE OwnerName = ?;");
                        ps.setString(1, data);
                        ps.setString(2, player.getName());
                        ps.executeUpdate();
                    }

				/*for (PetData pd : list) {
                    PreparedStatement ps4 = con.prepareStatement("INSERT INTO Pets (OwnerName, " + s + "" + pd.toString() + ") VALUES (?, ?);");
					ps4.setString(1, player.getName());
					ps4.setString(2, b.toString());
					ps4.executeUpdate();
				}*/
                } catch (SQLException e) {
                    Logger.log(Logger.LogLevel.SEVERE, "Failed to save Pet data for " + player.getName() + " to MySQL Database", e, true);
                } finally {
                    // Close the connection
				/*try {
					con.close();
				} catch (SQLException e) {
					EchoPet.getInstance().severe(e, "Failed to close connection to MySQL Database (" + player.getName() + ")");
				}*/
                }
            }
        }
    }

    public void saveToDatabase(LivingPet p, boolean isMount) {
        if (EchoPet.getInstance().options.useSql()) {
            Connection con = EchoPet.getInstance().getSqlCon();
            String mountPrefix = isMount ? "Mount" : "";

            if (con != null && p != null) {
                try {
                    // Delete any existing info
                    if (!isMount) {
                        this.clearFromDatabase(p.getOwner());
                    }

                    String dataList = SQLUtil.serialiseDataList(p.getActiveData(), isMount);
                    String data1 = SQLUtil.serialiseDataListBooleans(p.getActiveData(), true);

                    String sql;
                    if ((!dataList.equalsIgnoreCase("") && !data1.equalsIgnoreCase(""))) {
                        sql = "INSERT INTO Pets (OwnerName, " + mountPrefix + "PetType, " + mountPrefix + "PetName, " + dataList + ") " +
                                "VALUES (?)";
                    } else {
                        sql = "INSERT INTO Pets (OwnerName, " + mountPrefix + "PetType, " + mountPrefix + "PetName) " +
                                "VALUES (?)";
                    }

                    String duplicate = "ON DUPLICATE KEY UPDATE " + mountPrefix + "PetType='" + p.getPetType().toString() + "', " + mountPrefix + "PetName='" + p.getNameToString() + "'";

                    //PreparedStatement ps = con.prepareStatement(sql);
                    String state = "'" + p.getOwner().getName() + "', '" + p.getPetType().toString() + "', '" + p.getNameToString() + "'";

                    state = state + (data1.equalsIgnoreCase("") ? "" : ", " + data1);
                    duplicate = duplicate + SQLUtil.serialiseUpdate(p.getActiveData(), true, isMount);

                    con.createStatement().executeUpdate(sql.replace("?", state) + " " + duplicate + ";");


                    this.saveToDatabase(p.getMount(), true);

                } catch (SQLException e) {
                    Logger.log(Logger.LogLevel.SEVERE, "Failed to save Pet data for " + p.getOwner().getName() + " to MySQL Database", e, true);
                } finally {
                    // Close the connection
				/*try {
					con.close();
				} catch (SQLException e) {
					EchoPet.getInstance().severe(e, "Failed to close connection to MySQL Database (" + p.getOwner().getName() + ")");
				}*/
                }
            }
        }
    }

    public LivingPet createPetFromDatabase(Player p) {
        if (EchoPet.getInstance().options.useSql()) {
            Connection con = EchoPet.getInstance().getSqlCon();

            LivingPet pet = null;
            Player owner;
            PetType pt;
            String name;
            HashMap<PetData, Boolean> map = new HashMap<PetData, Boolean>();

            if (con != null) {
                try {
                    PreparedStatement ps = con.prepareStatement("SELECT * FROM Pets WHERE OwnerName = ?;");
                    ps.setString(1, p.getName());
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
                        pet.setName(name);
                        PetData[] PDT = createArray(map, true);
                        PetData[] PDF = createArray(map, false);
                        if (PDT != null) {
                            PetHandler.getInstance().setData(pet, PDT, true);
                        }
                        if (PDF != null) {
                            PetHandler.getInstance().setData(pet, PDF, false);
                        }

                        if (rs.getString("MountPetType") != null) {
                            PetType mt = findPetType(rs.getString("MountPetType"));
                            if (mt == null) {
                                return null;
                            }
                            String mName = rs.getString("MountPetName").replace("\'", "'");
                            for (PetData pd : PetData.values()) {
                                if (rs.getString("Mount" + pd.toString()) != null) {
                                    map.put(pd, Boolean.valueOf(rs.getString("Mount" + pd.toString())));
                                }
                            }

                            LivingPet mount = pet.createMount(mt, false);
                            if (mount != null) {
                                mount.setName(mName);
                                PetData[] MDT = createArray(map, true);
                                PetData[] MDF = createArray(map, false);

                                if (MDT != null) {
                                    ph.setData(mount, MDT, true);
                                }
                                if (MDF != null) {
                                    ph.setData(mount, MDF, false);
                                }
                            }
                        }
                    }
                } catch (SQLException e) {
                    Logger.log(Logger.LogLevel.SEVERE, "Failed to retrieve Pet data for " + p.getName() + " in MySQL Database", e, true);
                } finally {
                    // Close the connection
				/*try {
					con.close();
				} catch (SQLException e) {
					EchoPet.getInstance().severe(e, "Failed to close connection to MySQL Database (" + p.getName() + ")");
				}*/
                }
            }


            return pet;
        }
        return null;
    }

    private PetData[] createArray(HashMap<PetData, Boolean> map, boolean b) {
        ArrayList<PetData> list = new ArrayList<PetData>();
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

    public void clearFromDatabase(Player p) {
        clearFromDatabase(p.getName());
    }

    public void clearFromDatabase(String name) {
        if (EchoPet.getInstance().options.useSql()) {
            Connection con = EchoPet.getInstance().getSqlCon();

            if (con != null) {
                try {
                    PreparedStatement ps1 = con.prepareStatement("DELETE FROM Pets WHERE OwnerName = ?;");
                    ps1.setString(1, name);
                    ps1.executeUpdate();
                } catch (SQLException e) {
                    Logger.log(Logger.LogLevel.SEVERE, "Failed to retrieve Pet data for " + name + " in MySQL Database", e, true);
                } finally {
                    // Close the connection
				/*try {
					con.close();
				} catch (SQLException e) {
					EchoPet.getInstance().severe(e, "Failed to close connection to MySQL Database (" + p.getName() + ")");
				}*/
                }
            }
        }
    }

    public void clearMountFromDatabase(String name) {
        if (EchoPet.getInstance().options.useSql()) {
            Connection con = EchoPet.getInstance().getSqlCon();

            if (con != null) {
                try {
                    ArrayList<PetData> arrayList = new ArrayList<PetData>();
                    for (PetData pd : PetData.values()) {
                        arrayList.add(pd);
                    }
                    String list = SQLUtil.serialiseUpdate(arrayList, null, true);
                    PreparedStatement ps = con.prepareStatement("UPDATE Pets SET ? WHERE OwnerName = ?;");
                    ps.setString(1, list);
                    ps.setString(2, name);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    Logger.log(Logger.LogLevel.SEVERE, "Failed to retrieve Pet data for " + name + " in MySQL Database", e, true);
                } finally {
                    // Close the connection
				/*try {
					con.close();
				} catch (SQLException e) {
					EchoPet.getInstance().severe(e, "Failed to close connection to MySQL Database (" + p.getName() + ")");
				}*/
                }
            }
        }
    }
}