package com.github.dsh105.echopet.api.event;


import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.dsh105.echopet.entity.pet.Pet;

public class PetSpawnEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	
	private Pet pet;
	private Location spawnLocation;
	
	public PetSpawnEvent(Pet pet, Location spawnLocation) {
		this.pet = pet;
		this.spawnLocation = spawnLocation;
	}

	/**
	 * Gets the {@link Pet} involved in this event
	 *
	 * @return the {@link Pet} involved
	 */
	public Pet getPet() {
		return this.pet;
	}
	
	public Location getSpawnLocation() {
		return this.spawnLocation;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	/**
	 * Gets the cancellation state of this event. A cancelled event will not
	 * be executed in the server, but will still pass to other plugins
	 *
	 * @return true if this event is cancelled
	 */
	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	/**
	 * Sets the cancellation state of this event. A cancelled event will not
	 * be executed in the server, but will still pass to other plugins
	 *
	 * @param cancel true if you wish to cancel this event
	 */
	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
