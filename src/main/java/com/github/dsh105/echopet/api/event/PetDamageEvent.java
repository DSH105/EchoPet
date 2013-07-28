package com.github.dsh105.echopet.api.event;

import com.github.dsh105.echopet.entity.pet.Pet;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PetDamageEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = true; // cancelled by default

	private Pet pet;
	private double damage;
	private DamageCause damageCause;

	public PetDamageEvent(Pet pet, DamageCause damageCause, final double damage) {
		this.pet = pet;
		this.damage = damage;
		this.damageCause = damageCause;
	}

	public double getDamage() {
		return this.damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	/**
	 * Gets the {@link Pet} involved in this event
	 *
	 * @return the {@link Pet} involved
	 */
	public Pet getPet() {
		return this.pet;
	}

	public DamageCause getDamageCause() {
		return this.damageCause;
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