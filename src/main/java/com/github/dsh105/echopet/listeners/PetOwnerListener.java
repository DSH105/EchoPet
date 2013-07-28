package com.github.dsh105.echopet.listeners;

import com.github.dsh105.echopet.api.event.PetInteractEvent;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.entity.pet.CraftPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.util.Lang;

public class PetOwnerListener implements Listener {
	
	private EchoPet ec;
	
	public PetOwnerListener(EchoPet ec) {
		this.ec = ec;
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Player p = event.getPlayer();
		Entity e = event.getRightClicked();
		if (e instanceof CraftPet) {
			Pet pet = ((CraftPet) e).getPet();
			event.setCancelled(true);
			PetInteractEvent iEvent = new PetInteractEvent(pet, p, PetInteractEvent.Action.RIGHT_CLICK, false);
			EchoPet.getPluginInstance().getServer().getPluginManager().callEvent(iEvent);
			if (!iEvent.isCancelled()) {
				pet.getEntityPet().a(((CraftPlayer) p).getHandle());
			}
		}
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player p = event.getPlayer();
		final Pet pi = ec.PH.getPet(p);
		if (pi != null) {
			if (event.getFrom().getWorld() == event.getTo().getWorld()) {
				new BukkitRunnable() {

					@Override
					public void run() {
						pi.teleportToOwner();
					}

				}.runTaskLater(EchoPet.getPluginInstance(), 5L);
				pi.teleportToOwner();
			}
			else {
				ec.PH.removePets(p); // Safeguard for Multiworld travel
				p.sendMessage(Lang.DIMENSION_CHANGE.toString());
				loadPets(p, false);
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		Pet pi = ec.PH.getPet(p);
		if (pi != null) {
			//ec.PH.saveFileData("autosave", pi);
			ec.PH.saveFileData("autosave", pi);
			ec.SPH.saveToDatabase(pi, false);
			ec.PH.removePet(pi);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		if (ec.update && p.hasPermission("echopet.update")) {
			p.sendMessage(ec.prefix + ChatColor.GOLD + "An update is available: " + ec.name + " (" + ec.size + " bytes).");
			p.sendMessage(ec.prefix + ChatColor.GOLD + "Type /ecupdate to update.");
		}
		new BukkitRunnable() {
			
			public void run() {
				loadPets(p, true);
			}
			
		}.runTaskLater(ec, 20);
	}
	
	public static void loadPets(Player p, boolean sendMessage) {
		EchoPet ec = EchoPet.getPluginInstance();
		if (ec.DO.sqlOverride()) {
			Pet pet = ec.SPH.createPetFromDatabase(p);
			if (pet == null) {
				return;
			}
			else {
				if (sendMessage) {
					p.sendMessage(Lang.DATABASE_PET_LOAD.toString().replace("%petname%", pet.getPetName().toString()));
				}
			}
			return;
		}
		
		if (ec.getPetConfig().get("default." + p.getName() + ".pet.type") != null) {
			Pet pi = ec.PH.createPetFromFile("default", p);
			if (pi == null) {
				return;
			}
			else {
				if (sendMessage) {
					p.sendMessage(Lang.DEFAULT_PET_LOAD.toString().replace("%petname%", pi.getPetName().toString()));
				}
			}
			return;
		}
		
		if (ec.DO.autoLoadPets(p)) {
			if (ec.getPetConfig().get("autosave." + p.getName() + ".pet.type") != null) {
				Pet pi = ec.PH.createPetFromFile("autosave", p);
				if (pi == null) {
					return;
				}
				else {
					if (sendMessage) {
						p.sendMessage(Lang.AUTOSAVE_PET_LOAD.toString().replace("%petname%", pi.getPetName().toString()));
					}
				}
				return;
			}
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();
		Pet pet = ec.PH.getPet(p); 
		if (pet != null) {
			ec.PH.saveFileData("autosave", pet);
			ec.SPH.saveToDatabase(pet, false);
			ec.PH.removePet(pet);
			//p.sendMessage(Lang.REMOVE_PET_DEATH.toString());
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player p = event.getPlayer();
		Pet pet = ec.PH.getPet(p);
		if (pet != null) {
			loadPets(p, false);
		}
	}
}