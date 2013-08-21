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

	/**
	 * Gets the {@link Pet} involved in this event
	 *
	 * @return the {@link Pet} involved
	 */
	public Pet getPet() {
		return this.pet;
	}

	/**
	 * Gets the player that interated with the Pet
	 *
	 * @return
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * Gets the action executed by the {@link Player}
	 *
	 * @return the {@link Action} of the event
	 */
	public Action getAction() {
		return this.action;
	}

	/**
	 * Returns whether the {@link Player} that interacted was the Pet's owner
	 *
	 * @return true if it is the owner
	 */
	public boolean isPlayerOwner() {
		return this.player == this.pet.getOwner();
	}

	/**
	 * Gets the cancellation state of this event. A cancelled event will not
	 * be executed in the server, but will still pass to other plugins
	 *
	 * @return true if this event is cancelled
	 */
	public boolean isCancelled() {
		return this.cancelled;
	}

	/**
	 * Sets the cancellation state of this event. A cancelled event will not
	 * be executed in the server, but will still pass to other plugins
	 *
	 * @param cancel true if you wish to cancel this event
	 */
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public enum Action {
		/**
		 * Left clicking a Pet
		 */
		LEFT_CLICK,

		/**
		 * Right clicking a Pet
		 */
		RIGHT_CLICK;
	}
}