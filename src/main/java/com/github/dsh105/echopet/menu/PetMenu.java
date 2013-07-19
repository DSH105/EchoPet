package com.github.dsh105.echopet.menu;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.util.Lang;
import com.github.dsh105.echopet.util.StringUtil;


public class PetMenu implements Menu {
	
	Inventory inv;
	private Pet pet;
	private ArrayList<MenuOption> options = new ArrayList<MenuOption>();
	
	public PetMenu(Pet pet, ArrayList<MenuOption> options) {
		this.pet = pet;
		this.inv = Bukkit.createInventory(pet.getOwner(), 9, "EchoPet DataMenu");
		this.options = options;
		for (MenuOption o : this.options) {
			this.inv.setItem(o.position, o.item.getItem());
		}
		this.inv.setItem(8, DataMenuItem.CLOSE.getItem());
	}
	
	public void open(boolean sendMessage) {
		this.pet.getOwner().openInventory(this.inv);
		if (sendMessage) {
			this.pet.getOwner().sendMessage(Lang.OPEN_MENU.toString().replace("%type%", StringUtil.capitalise(this.pet.getPetType().toString().replace("_", " "))));
		}
	}
}
