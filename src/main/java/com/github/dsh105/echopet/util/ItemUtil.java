package com.github.dsh105.echopet.util;

import net.minecraft.server.v1_6_R2.NBTTagCompound;
import net.minecraft.server.v1_6_R2.NBTTagList;
import org.bukkit.craftbukkit.v1_6_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class ItemUtil {

	public static ItemStack addEnchantmentGlow(ItemStack itemStack) {
		net.minecraft.server.v1_6_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
		NBTTagCompound tag = null;
		if (!nmsStack.hasTag()) {
			tag = new NBTTagCompound();
			nmsStack.setTag(tag);
		}
		if (tag == null) {
			tag = nmsStack.getTag();
		}
		NBTTagList ench = new NBTTagList();
		tag.set("ench", ench);
		nmsStack.setTag(tag);
		return CraftItemStack.asCraftMirror(nmsStack);
	}

	public static ItemStack removeEnchantmentGlow(ItemStack itemStack) {
		net.minecraft.server.v1_6_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
		NBTTagCompound tag = null;
		if (!nmsStack.hasTag()) {
			return itemStack;
		}
		tag = nmsStack.getTag();
		tag.set("ench", null);
		nmsStack.setTag(tag);
		return CraftItemStack.asCraftMirror(nmsStack);
	}
}