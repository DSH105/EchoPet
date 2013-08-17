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
