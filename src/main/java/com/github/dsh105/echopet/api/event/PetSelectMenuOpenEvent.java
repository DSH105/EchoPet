package com.github.dsh105.echopet.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Project by DSH105
 */

public class PetSelectMenuOpenEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;

	private Player viewer;

	public PetSelectMenuOpenEvent(Player viewer) {
		this.viewer = viewer;
	}

	/**
	 * Gets the {@link Player} who opened the Menu
	 *
	 * @return Player that opened the Menu
	 */
	public Player getPlayer() {
		return this.viewer;
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