package com.github.dsh105.echopet.menu.selector;

import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.util.ItemUtil;
import com.github.dsh105.echopet.util.PetUtil;
import com.github.dsh105.echopet.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Project by DSH105
 */

public enum PetItem {

	BAT(PetType.BAT, Material.getMaterial(383), 1, (short) 65, true, "Bat Pet"),
	BLAZE(PetType.BLAZE, Material.getMaterial(383), 1, (short) 61, true, "Blaze Pet"),
	CAVESPIDER(PetType.CAVESPIDER, Material.getMaterial(383), 1, (short) 59, true, "Cave Spider Pet"),
	CHICKEN(PetType.CHICKEN, Material.getMaterial(383), 1, (short) 93, true, "Chicken Pet"),
	COW(PetType.COW, Material.getMaterial(383), 1, (short) 92, true, "Cow Pet"),
	CREEPER(PetType.CREEPER, Material.getMaterial(383), 1, (short) 50, true, "Creeper Pet"),
	ENDERDRAGON(PetType.ENDERDRAGON, Material.getMaterial(122), 1, (short) 0, true, "EnderDragon Pet"),
	ENDERMAN(PetType.ENDERMAN, Material.getMaterial(383), 1, (short) 58, true, "Enderman Pet"),
	GHAST(PetType.GHAST, Material.getMaterial(383), 1, (short) 56, true, "Ghast Pet"),
	GIANT(PetType.GIANT, Material.getMaterial(383), 1, (short) 54, true, "Giant Pet"),
	HORSE(PetType.HORSE, Material.getMaterial(383), 1, (short) 100, true, "Horse Pet"),
	IRONGOLEM(PetType.IRONGOLEM, Material.getMaterial(86), 1, (short) 0, true, "Iron Golem Pet"),
	MAGMACUBE(PetType.MAGMACUBE, Material.getMaterial(383), 1, (short) 62, true, "Magma Cube Pet"),
	MUSHROOMCOW(PetType.MUSHROOMCOW, Material.getMaterial(383), 1, (short) 96, true, "Mushroom Cow Pet"),
	OCELOT(PetType.OCELOT, Material.getMaterial(383), 1, (short) 98, true, "Ocelot Pet"),
	PIG(PetType.PIG, Material.getMaterial(383), 1, (short) 90, true, "Pig Pet"),
	PIGZOMBIE(PetType.PIGZOMBIE, Material.getMaterial(383), 1, (short) 57, true, "PigZombie Pet"),
	SHEEP(PetType.SHEEP, Material.getMaterial(383), 1, (short) 91, true, "Sheep Pet"),
	SILVERFISH(PetType.SILVERFISH, Material.getMaterial(383), 1, (short) 60, true, "Silverfish Pet"),
	SKELETON(PetType.SKELETON, Material.getMaterial(383), 1, (short) 51, true, "Skeleton Pet"),
	SLIME(PetType.SLIME, Material.getMaterial(383), 1, (short) 55, true, "Slime Pet"),
	SNOWMAN(PetType.SNOWMAN, Material.getMaterial(332), 1, (short) 0, true, "Snowman Pet"),
	SPIDER(PetType.SPIDER, Material.getMaterial(383), 1, (short) 52, true, "Spider Pet"),
	SQUID(PetType.SQUID, Material.getMaterial(383), 1, (short) 94, true, "Squid Pet"),
	VILLAGER(PetType.VILLAGER, Material.getMaterial(383), 1, (short) 120, true, "Villager Pet"),
	WITCH(PetType.WITCH, Material.getMaterial(383), 1, (short) 66, true, "Witch Pet"),
	WITHER(PetType.WITHER, Material.getMaterial(399), 1, (short) 0, true, "Wither Pet"),
	WOLF(PetType.WOLF, Material.getMaterial(383), 1, (short) 95, true, "Wolf Pet"),
	ZOMBIE(PetType.ZOMBIE, Material.getMaterial(383), 1, (short) 54, true, "Zombie Pet");

	public PetType petType;
	private Material mat;
	private int amount;
	private short data;
	private boolean glow;
	private String name;

	PetItem(PetType petType, Material mat, int amount, short data, boolean glow, String name) {
		this.petType = petType;
		this.mat = mat;
		this.amount = amount;
		this.data = data;
		this.glow = glow;
		this.name = name;
	}

	public ItemStack getItem(Player p) {
		ItemStack i = new ItemStack(this.mat, this.amount, this.data);
		ItemMeta meta = i.getItemMeta();
		boolean hasPerm = p.hasPermission("echopet.*") || p.hasPermission("echopet.pet.*") || p.hasPermission("echopet.pet.type.*") || p.hasPermission("echopet.pet.type." + PetUtil.getPetPerm(this.petType));
		meta.setDisplayName((hasPerm ? ChatColor.GREEN : ChatColor.RED) + this.name);
		//meta.setLore(this.lore);
		i.setItemMeta(meta);
		if (this.glow) {
			ItemUtil.addEnchantmentGlow(i);
		}
		return i;
	}
}