package com.github.dsh105.echopet.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PetRightClickEvent extends Event implements Cancellable {

	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setCancelled(boolean cancel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
