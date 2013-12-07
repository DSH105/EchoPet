package io.github.dsh105.echopet.listeners;

import io.github.dsh105.echopet.data.PetHandler;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkListener implements Listener {

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        for (Entity e : event.getChunk().getEntities()) {
            if (e instanceof EntityLivingPet) {
                PetHandler.getInstance().removePet(((EntityLivingPet) e).getPet(), true);
                if (!e.isDead()) {
                    ((EntityLivingPet) e).remove(true);
                }
            }
        }
    }
}