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

	/**
	 * Gets the Location this Pet moved to
	 *
	 * @return Location moved to
	 */
	public Location getTo() {
		return this.to;
	}

	/**
	 * Gets the Location this Pet moved from
	 *
	 * @return Location moved from
	 */
	public Location getFrom() {
		return this.from;
	}

	/**
	 * Sets the location that this Pet will move to
	 *
	 * @param to New Location this Pet will move to
	 */
	public void setTo(Location to) {
		this.to = to;
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

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
