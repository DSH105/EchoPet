package com.github.dsh105.echopet.menu.selector;

import com.github.dsh105.echopet.util.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Project by DSH105
 */

public enum SelectorItem {

	SELECTOR(Material.BONE, 1, (short) 0, false, ChatColor.GREEN + "Pet Selector"),
	TOGGLE(Material.BONE, 1, (short) 0, false, ChatColor.YELLOW + "Toggle Pet"),
	CALL(Material.ENDER_PEARL, 1, (short) 0, false, ChatColor.YELLOW + "Call Pet"),
	RIDE(Material.CARROT_STICK, 1, (short) 0, false, ChatColor.YELLOW + "Ride Pet"),
	HAT(Material.IRON_HELMET, 1, (short) 0, false, ChatColor.YELLOW + "Hat Pet"),
	MENU(Material.WORKBENCH, 1, (short) 0, false, ChatColor.YELLOW + "Open PetMenu"),
	CLOSE(Material.BOOK, 1, (short) 0 , false, ChatColor.YELLOW + "Close");

	private Material mat;
	private int amount;
	private short data;
	private boolean glow;
	private String name;

	SelectorItem(Material mat, int amount, short data, boolean glow, String name) {
		this.mat = mat;
		this.amount = amount;
		this.data = data;
		this.glow = glow;
		this.name = name;
	}

	public ItemStack getItem() {
		ItemStack i = new ItemStack(this.mat, this.amount, this.data);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(this.name);
		//meta.setLore(this.lore);
		i.setItemMeta(meta);
		if (this.glow) {
			ItemUtil.addEnchantmentGlow(i);
		}
		return i;
	}

	public ItemStack getItem(int amount) {
		ItemStack i = new ItemStack(this.mat, amount, this.data);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(this.name);
		//meta.setLore(this.lore);
		i.setItemMeta(meta);
		if (this.glow) {
			ItemUtil.addEnchantmentGlow(i);
		}
		return i;
	}
}