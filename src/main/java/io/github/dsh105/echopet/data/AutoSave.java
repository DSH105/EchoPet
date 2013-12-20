package io.github.dsh105.echopet.data;

import io.github.dsh105.echopet.EchoPet;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import org.bukkit.scheduler.BukkitRunnable;


public class AutoSave {

    public AutoSave(int timer) {
        new BukkitRunnable() {
            public void run() {
                EchoPet ec = EchoPet.getInstance();
                for (Pet pi : ec.PH.getPets()) {
                    ec.PH.saveFileData("autosave", pi);
                    ec.SPH.saveToDatabase(pi, false);
                }
            }
        }.runTaskTimer(EchoPet.getInstance(), (20 * timer) / 2, 20 * timer);
    }
}
