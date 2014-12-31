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

package com.dsh105.echopet.api.event.listeners;

import com.dsh105.commodus.container.PositionContainer;
import com.dsh105.echopet.api.configuration.Lang;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.event.Listen;
import com.dsh105.echopet.api.event.NullSpongeEvent;
import com.dsh105.echopet.api.hook.WorldGuardDependency;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.bridge.Ident;
import com.dsh105.echopet.bridge.PlayerBridge;
import com.dsh105.echopet.bridge.container.EventContainer;
import org.kitteh.vanish.event.VanishStatusChangeEvent;

import java.util.List;
import java.util.UUID;

public class DependencyListener {

    @Listen(bukkit = VanishStatusChangeEvent.class, sponge = NullSpongeEvent.class)
    public void onVanish(EventContainer event) {
        List<Pet> pets = EchoPet.getManager().getPetsFor(event.get(PlayerBridge.class).getUID());
        if (!pets.isEmpty()) {
            for (Pet pet : pets) {
                boolean vanishing = event.get(boolean.class);
                pet.setShouldVanish(vanishing);
                pet.getModifier().setInvisible(vanishing);
            }
        }
    }

    @Listen(bukkit = org.bukkit.event.player.PlayerMoveEvent.class, sponge = org.spongepowered.api.event.player.PlayerMoveEvent.class)
    public void onPlayerMove(EventContainer event) {
        PlayerBridge player = event.get(PlayerBridge.class);
        UUID playerUID = player.getUID();
        List<Pet> pets = EchoPet.getManager().getPetsFor(playerUID);
        if (!pets.isEmpty()) {
            if (!EchoPet.getDependency(WorldGuardDependency.class).allowPets(event.get(PositionContainer.class))) {
                EchoPet.getManager().removePets(playerUID);
                Lang.ENTER_PET_DISABLED_REGION.send(player);
            }
        }
    }
}