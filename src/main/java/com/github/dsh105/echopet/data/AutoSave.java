package com.github.dsh105.echopet.data;

import org.bukkit.scheduler.BukkitRunnable;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.entity.pet.Pet;


public class AutoSave {
	
	public AutoSave(final EchoPet ec, int timer) {
		new BukkitRunnable() {
			public void run() {
				for (Pet pi : ec.PH.getAllPetData()) {
					ec.PH.saveFileData("autosave", pi);
					/*if (ec.getDefaultOptions().useSql()) {
						try {
							saveSqlData(ec, pi);
						} catch (SQLException e) {
							ec.log("Failed to connect to SQL Database during Auto-Save process: + \n" + e.getMessage());
						}
					}*/
				}
			}
		}.runTaskTimer(ec, (20*timer)/2, 20*timer);
	}
	
	// Incomplete MySQL support
	/*public void saveSqlData(EchoPet ec, PetInfo pi) throws SQLException {
		String oName = pi.getOwner().getName();
		
		Statement s = ec.getSqlCon().getConnection().createStatement();
		ResultSet rs = s.executeQuery("SELECT * FROM pets WHERE PlayerName = '" + oName + "';");
		rs.next();
		if (rs.getString("PlayerName") == null) {
			
		}
		else {
			s.executeQuery("UPDATE pets" +
					"SET PlayerName='" + oName + "', ");
		}
	}*/
}
