package com.github.dsh105.echopet.menu.selector;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.api.event.PetSelectMenuOpenEvent;
import com.github.dsh105.echopet.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashSet;

/**
 * Project by DSH105
 */

public class PetSelector implements Menu {

	Inventory inv;
	private int size;
	private Player viewer;

	public PetSelector(int size, Player viewer) {
		this.inv = Bukkit.createInventory(viewer, size, "Pet Selector");
		this.size = size;
		this.viewer = viewer;
		setup();
	}

	public void setup() {
		int count = 0;
		for (PetItem item : PetItem.values()) {

			if (EchoPet.getPluginInstance().DO.allowPetType(item.petType)) {
				this.inv.setItem(count, item.getItem(this.viewer));
				count++;
			}
		}

		this.inv.setItem(this.size - 1, SelectorItem.CLOSE.getItem());
		this.inv.setItem(this.size - 3, SelectorItem.TOGGLE.getItem());
		this.inv.setItem(this.size - 4, SelectorItem.CALL.getItem());

		this.inv.setItem(this.size - 6, SelectorItem.HAT.getItem());
		this.inv.setItem(this.size - 7, SelectorItem.RIDE.getItem());

		this.inv.setItem(this.size - 9, SelectorItem.MENU.getItem());
	}

	public void open(boolean sendMessage) {
		PetSelectMenuOpenEvent menuEvent = new PetSelectMenuOpenEvent(this.viewer);
		EchoPet.getPluginInstance().getServer().getPluginManager().callEvent(menuEvent);
		if (menuEvent.isCancelled()) {
			return;
		}
		this.viewer.openInventory(this.inv);
	}
}