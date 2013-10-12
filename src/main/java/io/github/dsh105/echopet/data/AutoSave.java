package io.github.dsh105.echopet.data;

import io.github.dsh105.echopet.EchoPet;
import io.github.dsh105.echopet.entity.pet.Pet;
import org.bukkit.scheduler.BukkitRunnable;


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
        }.runTaskTimer(EchoPet.getPluginInstance(), (20 * timer) / 2, 20 * timer);
    }
}
