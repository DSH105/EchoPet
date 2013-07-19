package com.github.dsh105.echopet.api.event;


import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.dsh105.echopet.entity.pet.Pet;

public class PetSpawnEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final Pet pet;
	private final Location spawnLocation;
	
	public PetSpawnEvent(Pet pet, Location spawnLocation) {
		this.pet = pet;
		this.spawnLocation = spawnLocation;
	}
	
	public Pet getPet() {
		return this.pet;
	}
	
	public Location getSpawnLocation() {
		return this.spawnLocation;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

}
