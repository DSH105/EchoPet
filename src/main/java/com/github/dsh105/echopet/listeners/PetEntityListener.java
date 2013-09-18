package com.github.dsh105.echopet.listeners;

import com.github.dsh105.echopet.api.event.PetAttackEvent;
import com.github.dsh105.echopet.api.event.PetDamageEvent;
import com.github.dsh105.echopet.api.event.PetInteractEvent;
import net.minecraft.server.v1_6_R2.DamageSource;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

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

	@EventHandler
	public void onPetEnterPortal(EntityPortalEvent event) {
		Entity e = event.getEntity();
		if (e instanceof CraftPet) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		Entity e = event.getEntity();
		if (e instanceof CraftPet) {
			CraftPet craftPet = (CraftPet) e;
			PetDamageEvent damageEvent = new PetDamageEvent(craftPet.getPet(), event.getCause(), event.getDamage());
			EchoPet.getPluginInstance().getServer().getPluginManager().callEvent(damageEvent);
			event.setDamage(damageEvent.getDamage());
			event.setCancelled(damageEvent.isCancelled());
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
		else if (event.getDamager() instanceof CraftPet) {
			CraftPet craftPet = (CraftPet) event.getDamager();
			PetAttackEvent attackEvent = new PetAttackEvent(craftPet.getPet(), e, DamageSource.mobAttack(craftPet.getPet().getEntityPet()), event.getDamage());
			EchoPet.getPluginInstance().getServer().getPluginManager().callEvent(attackEvent);
			event.setDamage(attackEvent.getDamage());
			event.setCancelled(attackEvent.isCancelled());
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
