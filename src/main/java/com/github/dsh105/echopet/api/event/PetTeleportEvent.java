package com.github.dsh105.echopet.api.event;

import com.github.dsh105.echopet.entity.pet.Pet;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;

public class PetTeleportEvent extends PetMoveEvent {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;

	public PetTeleportEvent(Pet pet, Location from, Location to) {
		super(pet, from, to);
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