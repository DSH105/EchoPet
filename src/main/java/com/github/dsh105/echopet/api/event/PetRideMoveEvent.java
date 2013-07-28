package com.github.dsh105.echopet.api.event;

import com.github.dsh105.echopet.entity.pet.Pet;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PetRideMoveEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;

	private Pet pet;
	private float forwardSpeed;
	private float sidewardSpeed;

	public PetRideMoveEvent(Pet pet, float forwardSpeed, float sidewardSpeed) {
		this.pet = pet;
		this.forwardSpeed = forwardSpeed;
		this.sidewardSpeed = sidewardSpeed;
	}

	public Pet getPet() {
		return this.pet;
	}

	public float getForwardMotionSpeed() {
		return this.forwardSpeed;
	}

	public float getSidewardMotionSpeed() {
		return this.sidewardSpeed;
	}

	public void setForwardMotionSpeed(float forwardMotionSpeed) {
		this.forwardSpeed = forwardMotionSpeed;
	}

	public void setSidewardMotionSpeed(float sidewardMotionSpeed) {
		this.sidewardSpeed = sidewardMotionSpeed;
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