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

package io.github.dsh105.echopet.listeners;

import io.github.dsh105.echopet.entity.CraftPet;
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
                if (e instanceof CraftPet) {
                    e.remove();
                    event.setCancelled(true);
                }
            }
        }
    }

    /*@EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        for (int i = 0; i < ((CraftChunk) event.getChunk()).getHandle().entitySlices.length; ++i) {
            @SuppressWarnings("unchecked")
            java.util.Iterator<Object> iter = ((CraftChunk) event.getChunk()).getHandle().entitySlices[i].iterator();
            while (iter.hasNext()) {
                try {
                    Entity entity = (Entity) iter.next();
                    if (entity != null && entity instanceof EntityPet) {
                        PetHandler.getInstance().removePet(((EntityPet) entity).getPet(), false);
                        iter.remove();
                        event.setCancelled(true);
                        event.getChunk().unload();
                    }
                } catch (NoSuchElementException e) {
                    continue;
                }
            }
        }
    }*/

    /*@EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        for (int i = 0; i < ((CraftChunk) event.getChunk()).getHandle().entitySlices.length; ++i) {
            java.util.Iterator<Object> iter = ((CraftChunk) event.getChunk()).getHandle().entitySlices[i].iterator();
            while (iter.hasNext()) {
                Entity entity = (Entity) iter.next();
                if (entity instanceof EntityLivingPet) {
                    PetHandler.getInstance().removePet(((EntityLivingPet) entity).getPet(), false);
                    iter.remove();
                    ConsoleLogger.log(Logger.LogLevel.NORMAL, "Removed pet entity from loaded chunk.");
                }
            }
        }
    }*/
}