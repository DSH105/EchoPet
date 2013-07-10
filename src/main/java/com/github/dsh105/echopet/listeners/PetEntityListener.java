package com.github.dsh105.echopet.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityInteractEvent;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.entity.pet.CraftPet;

public class PetEntityListener implements Listener {
	
	private EchoPet ec;
	
	public PetEntityListener(EchoPet ec) {
		this.ec = ec;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		Entity e = event.getEntity();
		if (e instanceof CraftPet) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreatureSpawnUnBlock(CreatureSpawnEvent event) {
		Entity e = event.getEntity();
		if (e instanceof CraftPet) {
			if (event.isCancelled()) {
				event.setCancelled(false);
			}
		}
	}
	
	// Stops pets getting damaged. Invincible pets :D
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		Entity e = event.getEntity();
		if (e instanceof CraftPet) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityInteract(EntityInteractEvent event) {
		Entity e = event.getEntity();
		if (e instanceof CraftPet) {
			event.setCancelled(true);
		}
	}
}
