package com.github.dsh105.echopet.api.event;

import com.github.dsh105.echopet.entity.pet.Pet;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PetInteractEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;

	private Pet pet;
	private Action action;

	public PetInteractEvent(Pet pet, Action action) {
		this.pet = pet;
		this.action = action;
	}

	public Pet getPet() {
		return this.pet;
	}

	public Action getAction() {
		return this.action;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public enum Action {
		LEFT_CLICK,
		RIGHT_CLICK;
	}
}