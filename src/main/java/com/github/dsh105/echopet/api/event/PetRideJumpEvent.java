package com.github.dsh105.echopet.api.event;

import com.github.dsh105.echopet.entity.pet.Pet;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PetRideJumpEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;

	private Pet pet;
	private double jumpHeight;

	public PetRideJumpEvent(Pet pet, final double jumpHeight) {
		this.pet = pet;
		this.jumpHeight = jumpHeight;
	}

	public Pet getPet() {
		return this.pet;
	}

	public double getJumpHeight() {
		return this.jumpHeight;
	}

	public void setJumpHeight(double jumpHeight) {
		this.jumpHeight = jumpHeight;
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