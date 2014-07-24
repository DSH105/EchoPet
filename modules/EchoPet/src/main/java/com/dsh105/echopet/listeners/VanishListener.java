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

package com.dsh105.echopet.listeners;

import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.plugin.EchoPet;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.kitteh.vanish.event.VanishStatusChangeEvent;

import java.util.List;

public class VanishListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onVanish(VanishStatusChangeEvent event) {
        Player owner = event.getPlayer();
        List<Pet> pets = EchoPet.getManager().getPetsFor(owner);
        if (!pets.isEmpty()) {
            for (Pet pet : pets) {
                pet.setShouldVanish(event.isVanishing());
                pet.getEntity().setInvisible(event.isVanishing());
            }
        }
    }
}