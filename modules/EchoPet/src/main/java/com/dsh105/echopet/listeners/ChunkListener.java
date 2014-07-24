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

import com.captainbern.minecraft.conversion.BukkitUnwrapper;
import com.dsh105.echopet.api.entity.entitypet.EntityPet;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (event.getChunk().getEntities().length > 0) {
            for (int i = 0; i < event.getChunk().getEntities().length; i++) {
                Entity entity = event.getChunk().getEntities()[i];
                if (entity instanceof LivingEntity) {
                    Object nmsEntity = BukkitUnwrapper.getInstance().unwrap(entity);
                    if (nmsEntity instanceof EntityPet) {
                        ((EntityPet) nmsEntity).getPet().despawn(false);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}