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

package io.github.dsh105.echopet.data;

import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.entity.Pet;
import org.bukkit.scheduler.BukkitRunnable;


public class AutoSave {

    public AutoSave(int timer) {
        new BukkitRunnable() {
            @Override
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
