package com.github.dsh105.echopet.listeners;

import com.github.dsh105.echopet.api.event.PetInteractEvent;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		Entity e = event.getEntity();
		if (e instanceof CraftPet) {
			Entity damager = event.getDamager();
			if (damager instanceof Player) {
				PetInteractEvent iEvent = new PetInteractEvent(((CraftPet) e).getPet(), (Player) damager, PetInteractEvent.Action.LEFT_CLICK, true);
				EchoPet.getPluginInstance().getServer().getPluginManager().callEvent(iEvent);
				event.setCancelled(iEvent.isCancelled());
			}
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
