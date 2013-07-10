package com.github.dsh105.echopet.listeners;


import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.entity.pet.CraftPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.util.Lang;
import com.github.dsh105.echopet.util.StringUtil;

public class WorldChangeListener implements Listener {
	
	private EchoPet ec;
	
	public WorldChangeListener(EchoPet ec) {
		this.ec = ec;
	}
	
	// Multi-World Transportation previously resulted in the 'lost entity' error
	// Removing the pet before that happens seems to fix the issue
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		final Player p = event.getPlayer();
		Pet pi = ec.PH.getPet(p);
		if (pi != null) {
			ec.PH.removePets(p); // Safeguard for Multiworld travel
			p.sendMessage(Lang.DIMENSION_CHANGE.toString());
			//pi.getPet().travelDimension();
		}
		
		if (ec.getPetConfig().get("default." + p.getName() + ".pet.type") != null) {
			Pet pet = ec.PH.createPetFromFile("default", p);
			if (pi == null) {
				ec.PH.removePet(ec.PH.getPet(p));
			}
			else {
				p.sendMessage(Lang.DEFAULT_PET_LOAD.toString().replace("%petname%", pet.getPetName().toString()));
			}
			return;
		}
		if (ec.DO.autoLoadPets(p)) {
			String w = p.getWorld().getName();
			if (ec.getPetConfig().get("autosave." + "." + w + "." + p.getName() + ".pet.type") != null) {
				Pet pet = ec.PH.createPetFromFile("autosave", p);
				if (pet == null) {
					ec.PH.removePet(ec.PH.getPet(p));
				}
				else {
					p.sendMessage(Lang.AUTOSAVE_PET_LOAD.toString().replace("%world%", w).replace("%petname%", StringUtil.capitalise(pet.getPetName().toString())));
				}
				return;
			}
		}
	}
	
	// Pets used to be able to enter portals.
	// Not very handy seeing as world to world teleportation isn't supported as of yet
	@EventHandler
	public void onPetEnterPortal(EntityPortalEvent event) {
		Entity e = event.getEntity();
		if (e instanceof CraftPet) {
			event.setCancelled(true);
		}
	}
}
