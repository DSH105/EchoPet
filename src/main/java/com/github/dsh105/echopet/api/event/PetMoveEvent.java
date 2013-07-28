package com.github.dsh105.echopet.api.event;

import com.github.dsh105.echopet.entity.pet.Pet;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PetMoveEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;

	private Pet pet;
	private Location from;
	private Location to;

	public PetMoveEvent(Pet pet, Location from, Location to) {
		this.pet = pet;
		this.from = from;
		this.to = to;
	}

	public Location getTo() {
		return this.to;
	}

	public Location getFrom() {
		return this.from;
	}

	public void setTo(Location to) {
		this.to = to;
	}

	public Pet getPet() {
		return this.pet;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return this.handlers;
	}
}
