package io.github.dsh105.echopet.data;

import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.entity.Pet;
import org.bukkit.scheduler.BukkitRunnable;


public class AutoSave {

    public AutoSave(int timer) {
        new BukkitRunnable() {
            public void run() {
                EchoPetPlugin ec = EchoPetPlugin.getInstance();
                for (Pet pi : ec.PH.getPets()) {
                    ec.PH.saveFileData("autosave", pi);
                    ec.SPH.saveToDatabase(pi, false);
                }
            }
        }.runTaskTimer(EchoPetPlugin.getInstance(), (20 * timer) / 2, 20 * timer);
    }
}
