package com.github.dsh105.echopet.listeners;


import com.github.dsh105.echopet.data.PetHandler;
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

		PetHandler.getInstance().loadPets(p, true, false, true);
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
