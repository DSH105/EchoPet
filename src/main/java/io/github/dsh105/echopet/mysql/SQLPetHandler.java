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

    public void updateDatabase(Player player, List<PetData> list, Boolean result, boolean isMount) {
        if (EchoPetPlugin.getInstance().options.useSql()) {
            Connection con = null;
            Statement statement = null;

            if (EchoPetPlugin.getInstance().dbPool != null) {
                try {
                    Map<String, String> updates = SQLUtil.constructUpdateMap(list, result, isMount);
                    if (!updates.isEmpty()) {
                        con = EchoPetPlugin.getInstance().dbPool.getConnection();
                        statement = con.createStatement();
                        for (Map.Entry<String, String> updateEntry : updates.entrySet()) {
                            statement.executeUpdate("UPDATE Pets SET " + updateEntry.getKey() + "='" + updateEntry.getValue() + "' WHERE OwnerName = '" + player.getName() + "'");
                        }
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

    public void saveToDatabase(Pet p, boolean isMount) {
        if (EchoPetPlugin.getInstance().options.useSql()) {
            Connection con = null;
            PreparedStatement ps = null;

            if (EchoPetPlugin.getInstance().dbPool != null && p != null) {
                try {
                    con = EchoPetPlugin.getInstance().dbPool.getConnection();
                    // Delete any existing info
                    if (!isMount) {
                        this.clearFromDatabase(p.getOwner());
                    }

                    // Deal with the pet metadata first
                    // This tends to be more problematic, so by shoving it out of the way, we can get the pet data saved.
                    if (isMount)
                        ps = con.prepareStatement("INSERT INTO Pets (OwnerName, MountPetType, MountPetName) VALUES (?, ?, ?)");
                    else
                        ps = con.prepareStatement("INSERT INTO Pets (OwnerName, PetType, PetName) VALUES (?, ?, ?)");

                    ps.setString(1, p.getOwner().getName());
                    ps.setString(2, p.getPetType().toString());
                    ps.setString(3, p.getName());
                    ps.executeUpdate();

                    this.updateDatabase(p.getOwner(), p.getPetData(), true, isMount);

                    this.saveToDatabase(p.getMount(), true);

                } catch (SQLException e) {
                    Logger.log(Logger.LogLevel.SEVERE, "Failed to save Pet data for " + p.getOwner().getName() + " to MySQL Database", e, true);
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

    public Pet createPetFromDatabase(Player p) {
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

                            Pet mount = pet.createMount(mt, false);
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

    public void clearFromDatabase(Player p) {
        clearFromDatabase(p.getName());
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

    public void clearMountFromDatabase(String name) {
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