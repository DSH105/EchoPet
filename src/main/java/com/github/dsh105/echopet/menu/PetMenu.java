package com.github.dsh105.echopet.menu;

import java.util.ArrayList;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.api.event.PetMenuOpenEvent;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.util.Lang;
import com.github.dsh105.echopet.util.StringUtil;


public class PetMenu implements Menu {
	
	Inventory inv;
	private int size;
	private Pet pet;
	private ArrayList<MenuOption> options = new ArrayList<MenuOption>();
	
	public PetMenu(Pet pet, ArrayList<MenuOption> options, int size) {
		this.pet = pet;
		this.size = size;
		this.inv = Bukkit.createInventory(pet.getOwner(), size, "EchoPet DataMenu");
		this.options = options;
		for (MenuOption o : this.options) {
			this.inv.setItem(o.position, o.item.getItem());
		}
		int book = size - 1;
		this.inv.setItem(book, DataMenuItem.CLOSE.getItem());
	}
	
	public void open(boolean sendMessage) {
		PetMenuOpenEvent menuEvent = new PetMenuOpenEvent(this.pet, PetMenuOpenEvent.MenuType.MAIN);
		EchoPet.getPluginInstance().getServer().getPluginManager().callEvent(menuEvent);
		if (menuEvent.isCancelled()) {
			return;
		}
		this.pet.getOwner().openInventory(this.inv);
		if (sendMessage) {
			this.pet.getOwner().sendMessage(Lang.OPEN_MENU.toString().replace("%type%", StringUtil.capitalise(this.pet.getPetType().toString().replace("_", " "))));
		}
	}
}
