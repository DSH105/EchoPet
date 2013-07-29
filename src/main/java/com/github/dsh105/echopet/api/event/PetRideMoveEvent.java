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

	/**
	 * Gets the {@link Pet} involved in this event
	 *
	 * @return the {@link Pet} involved
	 */
	public Pet getPet() {
		return this.pet;
	}

	/**
	 * Gets the motion speed moved forward
	 *
	 * @return forward motion speed
	 */
	public float getForwardMotionSpeed() {
		return this.forwardSpeed;
	}

	/**
	 * Gets the motion speed moved sidewards
	 *
	 * @return sideward motion speed
	 */
	public float getSidewardMotionSpeed() {
		return this.sidewardSpeed;
	}

	/**
	 * Sets the motion speed moved forward
	 *
	 * @param forwardMotionSpeed new forward motion speed
	 */
	public void setForwardMotionSpeed(float forwardMotionSpeed) {
		this.forwardSpeed = forwardMotionSpeed;
	}

	/**
	 * Sets the motion speed moved forward
	 *
	 * @param sidewardMotionSpeed new forward motion speed
	 */
	public void setSidewardMotionSpeed(float sidewardMotionSpeed) {
		this.sidewardSpeed = sidewardMotionSpeed;
	}

	/**
	 * Gets the cancellation state of this event. A cancelled event will not
	 * be executed in the server, but will still pass to other plugins
	 *
	 * @return true if this event is cancelled
	 */
	@Override
	public boolean isCancelled() {
		return cancelled;
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

	@Override
	public HandlerList getHandlers() {
		return this.handlers;
	}
}