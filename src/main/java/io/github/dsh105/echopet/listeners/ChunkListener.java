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