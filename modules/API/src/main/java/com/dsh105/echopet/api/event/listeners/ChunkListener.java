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

import com.captainbern.minecraft.conversion.BukkitUnwrapper;
import com.dsh105.echopet.api.entity.entitypet.EntityPet;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.event.Listen;
import com.dsh105.echopet.bridge.container.EventContainer;

import java.util.List;

public class ChunkListener {

    @Listen(bukkit = org.bukkit.event.world.ChunkUnloadEvent.class, sponge = org.spongepowered.api.event.world.ChunkUnloadEvent.class)
    public void onChunkUnload(EventContainer event) {
        List<?> entities = event.get(List.class);
        for (Object entity : entities) {
            Object entityHandle = BukkitUnwrapper.getInstance().unwrap(entity);
            if (entityHandle instanceof EntityPet) {
                Pet pet = ((EntityPet) entityHandle).getPet();
                pet.moveToOwner();
                event.setCancelled(true);
            }
        }
    }
}