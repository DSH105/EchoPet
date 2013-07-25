package com.github.dsh105.echopet.api.event;

import com.github.dsh105.echopet.entity.pet.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PetInteractEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;

	private Pet pet;
	private Player player;
	private Action action;

	public PetInteractEvent(Pet pet, Player player, Action action, boolean cancelledByDefault) {
		this.pet = pet;
		this.action = action;
		this.cancelled = cancelledByDefault;
	}

	public Pet getPet() {
		return this.pet;
	}

	public Player getPlayer() {
		return this.player;
	}

	public Action getAction() {
		return this.action;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public boolean isPlayerOwner() {
		return this.player == this.pet.getOwner();
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