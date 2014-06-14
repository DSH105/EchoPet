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

import com.dsh105.echopet.compat.api.entity.nms.EntityPet;
import com.dsh105.echopet.compat.api.util.ReflectionUtil;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkListener implements Listener {

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (event.getChunk().getEntities().length > 0) {
            for (int i = 0; i < event.getChunk().getEntities().length; i++) {
                Entity e = event.getChunk().getEntities()[i];
                if (ReflectionUtil.getEntityHandle(e) instanceof EntityPet) {
                    e.remove();
                    event.setCancelled(true);
                }
            }
        }
    }
}