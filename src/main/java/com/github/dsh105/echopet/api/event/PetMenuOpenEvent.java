package com.github.dsh105.echopet.api.event;

import com.github.dsh105.echopet.entity.pet.Pet;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PetMenuOpenEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;

	private Pet pet;
	private MenuType menuType;

	public PetMenuOpenEvent(Pet pet, MenuType menuType) {
		this.pet = pet;
		this.menuType = menuType;
	}

	public Pet getPet() {
		return this.pet;
	}

	public MenuType getMenuType() {
		return this.menuType;
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

	public enum MenuType {
		MAIN,
		DATA;
	}
}