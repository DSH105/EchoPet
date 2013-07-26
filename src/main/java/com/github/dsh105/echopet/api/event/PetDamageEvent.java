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

	public Pet getPet() {
		return this.pet;
	}

	public DamageCause getDamageCause() {
		return this.damageCause;
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