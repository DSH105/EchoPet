package com.github.dsh105.echopet.mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import com.github.dsh105.echopet.util.SQLUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.data.PetData;
import com.github.dsh105.echopet.data.PetHandler;
import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.data.UnorganisedPetData;
import com.github.dsh105.echopet.entity.pet.Pet;

public class SQLPetHandler {

	public void updateDatabase(Player player, ArrayList<PetData> list, Boolean result, boolean isMount) {
		if (EchoPet.getPluginInstance().DO.useSql()) {
			Connection con = EchoPet.getPluginInstance().getSqlCon();

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
					EchoPet.getPluginInstance().severe(e, "Failed to save Pet data for " + player.getName() + " to MySQL Database");
				} finally {
					// Close the connection
				/*try {
					con.close();
				} catch (SQLException e) {
					EchoPet.getPluginInstance().severe(e, "Failed to close connection to MySQL Database (" + player.getName() + ")");
				}*/
				}
			}
		}
	}

	public void saveToDatabase(Pet p, boolean isMount) {
		if (EchoPet.getPluginInstance().DO.useSql()) {
			Connection con = EchoPet.getPluginInstance().getSqlCon();
			String mount = isMount ? "Mount" : "";

			if (con != null && p != null) {
				try {
					// Delete any existing info
					if (!isMount) {
						this.clearFromDatabase(p.getOwner());
					}

					String dataList = SQLUtil.serialiseDataList(p.getAllData(true), isMount);
					String dataList2 = SQLUtil.serialiseDataList(p.getAllData(false), isMount);
					String data1 = SQLUtil.serialiseDataListBooleans(p.getAllData(true), true);
					String data2 = SQLUtil.serialiseDataListBooleans(p.getAllData(false), false);

					boolean b = false;
					boolean bb = false;
					String sql;

					if ((!dataList.equalsIgnoreCase("") && !data1.equalsIgnoreCase("")) && (!dataList2.equalsIgnoreCase("") && !data2.equalsIgnoreCase(""))) {
						sql = "INSERT INTO Pets (OwnerName, " + mount + "PetType, " + mount + "PetName, " + dataList + ", " + dataList2 + ")" +
								"VALUES (?)";
						b = true;
						bb = true;
					}
					else if ((!dataList.equalsIgnoreCase("") && !data1.equalsIgnoreCase(""))) {
						sql = "INSERT INTO Pets (OwnerName, " + mount + "PetType, " + mount + "PetName, " + dataList + ") " +
								"VALUES (?)";
						b = true;
						bb = false;
					}
					else if ((!dataList2.equalsIgnoreCase("") && !data2.equalsIgnoreCase(""))) {
						sql = "INSERT INTO Pets (OwnerName, " + mount + "PetType, " + mount + "PetName, " + dataList2 + ") " +
								"VALUES (?)";
						b = false;
						bb = true;
					}
					else {
						sql = "INSERT INTO Pets (OwnerName, " + mount + "PetType, " + mount + "PetName) " +
								"VALUES (?)";
					}

					boolean case1 = b && bb;
					boolean case2 = b && !bb;
					boolean case3 = !b && bb;

					String duplicate = "ON DUPLICATE KEY UPDATE " + mount + "PetType='" + p.getPetType().toString() + "', " + mount + "PetName='" + p.getNameToString() + "'";

					//PreparedStatement ps = con.prepareStatement(sql);
					String state = "'" + p.getOwner().getName() + "', '" + p.getPetType().toString() + "', '" + p.getNameToString() + "'";
					if (case1) {
						state = state + ", " + data1 + ", " + data2;
						duplicate = duplicate + ", " + SQLUtil.serialiseUpdate(p.getAllData(true), true, isMount);
						duplicate = duplicate + ", " + SQLUtil.serialiseUpdate(p.getAllData(false), false, isMount);
					}
					else if (case2) {
						state = state + ", " + data1;
						duplicate = duplicate + ", " + SQLUtil.serialiseUpdate(p.getAllData(true), true, isMount);
					}
					else if (case3) {
						state = state + ", " + data2;
						duplicate = duplicate + ", " + SQLUtil.serialiseUpdate(p.getAllData(false), false, isMount);
					}
					con.createStatement().executeUpdate(sql.replace("?", state) + " " + duplicate + ";");
					//ps.setString(1, state);
					//ps.executeUpdate();

					this.saveToDatabase(p.getMount(), true);

				} catch (SQLException e) {
					EchoPet.getPluginInstance().severe(e, "Failed to save Pet data for " + p.getOwner().getName() + " to MySQL Database");
				} finally {
					// Close the connection
				/*try {
					con.close();
				} catch (SQLException e) {
					EchoPet.getPluginInstance().severe(e, "Failed to close connection to MySQL Database (" + p.getOwner().getName() + ")");
				}*/
				}
			}
		}
	}
	
	public Pet createPetFromDatabase(Player p) {
		if (EchoPet.getPluginInstance().DO.useSql()) {
			Connection con = EchoPet.getPluginInstance().getSqlCon();

			Pet pet = null;
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
						name = rs.getString("PetName");

						for (PetData pd : PetData.values()) {
							if (rs.getString(pd.toString()) != null) {
								map.put(pd, Boolean.valueOf(rs.getString(pd.toString())));
							}
						}

						if (owner == null) {
							return null;
						}

						PetHandler ph = PetHandler.getInstance();
						pet = ph.createPet(owner, pt);
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
							String mName = rs.getString("MountPetName");
							for (PetData pd : PetData.values()) {
								if (rs.getString("Mount" + pd.toString()) != null) {
									map.put(pd, Boolean.valueOf(rs.getString("Mount" + pd.toString())));
								}
							}

							Pet mount = pet.createMount(mt);
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
				} catch (SQLException e) {
					EchoPet.getPluginInstance().severe(e, "Failed to retrieve Pet data for " + p.getName() + " in MySQL Database");
				} finally {
					// Close the connection
				/*try {
					con.close();
				} catch (SQLException e) {
					EchoPet.getPluginInstance().severe(e, "Failed to close connection to MySQL Database (" + p.getName() + ")");
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

	public void saveToDatabase(Player p, UnorganisedPetData UPD, UnorganisedPetData UMD) {
		if (EchoPet.getPluginInstance().DO.useSql()) {
			PetType pt = UPD.petType;
			PetData[] data = UPD.petDataList.toArray(new PetData[UPD.petDataList.size()]);
			String petName = UPD.petName;
			if (UPD.petName == null || UPD.petName.equalsIgnoreCase("")) {
				petName = pt.getDefaultName(p);
			}
			PetType mountType = UMD.petType;
			PetData[] mountData = UMD.petDataList.toArray(new PetData[UMD.petDataList.size()]);
			String mountName = UMD.petName;
			if (UMD.petName == null || UMD.petName.equalsIgnoreCase("")) {
				mountName = pt.getDefaultName(p);
			}

			Connection con = EchoPet.getPluginInstance().getSqlCon();

			if (con != null) {
				try {
					PreparedStatement ps1 = con.prepareStatement("DELETE FROM Pets WHERE OwnerName = ?;");
					ps1.setString(1, p.getName());
					ps1.executeUpdate();


					PreparedStatement ps2 = con.prepareStatement("INSERT INTO Pets (OwnerName, PetType, PetName) VALUES (?, ?, ?);");
					ps2.setString(1, p.getName());
					ps2.setString(2, pt.toString());
					ps2.setString(3, petName);
					ps2.executeUpdate();


					for (PetData pd : data) {
						PreparedStatement ps3 = con.prepareStatement("INSERT INTO Pets (OwnerName, " + pd.toString() + ") VALUES (?, ?);");
						ps3.setString(1, p.getName());
						ps3.setString(2, "TRUE");
						ps3.executeUpdate();
					}


					PreparedStatement ps4 = con.prepareStatement("INSERT INTO Pets (OwnerName, MountPetType, MountPetName) VALUES (?, ?, ?);");
					ps4.setString(1, p.getName());
					ps4.setString(2, mountType.toString());
					ps4.setString(3, mountName);
					ps4.executeUpdate();


					for (PetData pd : mountData) {
						PreparedStatement ps5 = con.prepareStatement("INSERT INTO Pets (OwnerName, Mount" + pd.toString() + ") VALUES (?, ?);");
						ps5.setString(1, p.getName());
						ps5.setString(2, "TRUE");
						ps5.executeUpdate();
					}
				} catch (SQLException e) {
					EchoPet.getPluginInstance().severe(e, "Failed to save Pet data for " + p.getName() + " to MySQL Database");
				} finally {
					// Close the connection
				/*try {
					con.close();
				} catch (SQLException e) {
					EchoPet.getPluginInstance().severe(e, "Failed to close connection to MySQL Database (" + p.getName() + ")");
				}*/
				}
			}
		}

	}
	
	public void saveToDatabase(Player p, UnorganisedPetData UPD) {
		if (EchoPet.getPluginInstance().DO.useSql()) {
			PetType pt = UPD.petType;
			PetData[] data = UPD.petDataList.toArray(new PetData[UPD.petDataList.size()]);
			String petName = UPD.petName;
			if (UPD.petName == null || UPD.petName.equalsIgnoreCase("")) {
				petName = pt.getDefaultName(p);
			}

			Connection con = EchoPet.getPluginInstance().getSqlCon();

			if (con != null) {
				try {
					PreparedStatement ps1 = con.prepareStatement("DELETE FROM Pets WHERE OwnerName = ?;");
					ps1.setString(1, p.getName());
					ps1.executeUpdate();


					PreparedStatement ps2 = con.prepareStatement("INSERT INTO Pets (OwnerName, PetType, PetName) VALUES (?, ?);");
					ps2.setString(1, p.getName());
					ps2.setString(2, pt.toString());
					ps2.setString(3, petName);
					ps2.executeUpdate();


					for (PetData pd : data) {
						PreparedStatement ps3 = con.prepareStatement("INSERT INTO Pets (OwnerName, " + pd.toString() + ") VALUES (?, ?);");
						ps1.setString(1, p.getName());
						ps3.setString(2, "TRUE");
						ps3.executeUpdate();
					}
				} catch (SQLException e) {
					EchoPet.getPluginInstance().severe(e, "Failed to save Pet data for " + p.getName() + " to MySQL Database");
				} finally {
					// Close the connection
				/*try {
					con.close();
				} catch (SQLException e) {
					EchoPet.getPluginInstance().severe(e, "Failed to close connection to MySQL Database (" + p.getName() + ")");
				}*/
				}
			}
		}
	}

	public void clearFromDatabase(Player p) {
		if (EchoPet.getPluginInstance().DO.useSql()) {
			Connection con = EchoPet.getPluginInstance().getSqlCon();

			if (con != null) {
				try {
					PreparedStatement ps1 = con.prepareStatement("DELETE FROM Pets WHERE OwnerName = ?;");
					ps1.setString(1, p.getName());
					ps1.executeUpdate();
				} catch (SQLException e) {
					EchoPet.getPluginInstance().severe(e, "Failed to retrieve Pet data for " + p.getName() + " in MySQL Database");
				} finally {
					// Close the connection
				/*try {
					con.close();
				} catch (SQLException e) {
					EchoPet.getPluginInstance().severe(e, "Failed to close connection to MySQL Database (" + p.getName() + ")");
				}*/
				}
			}
		}
	}
}