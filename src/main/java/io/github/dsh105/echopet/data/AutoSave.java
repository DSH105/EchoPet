package com.github.dsh105.echopet.data;

import org.bukkit.scheduler.BukkitRunnable;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.entity.pet.Pet;


public class AutoSave {
	
	public AutoSave(int timer) {
		new BukkitRunnable() {
			public void run() {
				EchoPet ec = EchoPet.getPluginInstance();
				for (Pet pi : ec.PH.getPets()) {
					ec.PH.saveFileData("autosave", pi);
					ec.SPH.saveToDatabase(pi, false);
				}
			}
		}.runTaskTimer(EchoPet.getPluginInstance(), (20*timer)/2, 20*timer);
	}
}
