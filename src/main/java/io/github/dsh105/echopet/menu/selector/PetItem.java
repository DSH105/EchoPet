package io.github.dsh105.echopet.menu.selector;

import io.github.dsh105.echopet.data.PetType;
import io.github.dsh105.echopet.util.PetUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Project by DSH105
 */

public enum PetItem {

    BAT(PetType.BAT, Material.getMaterial(383), 1, (short) 65, true, "Bat"),
    BLAZE(PetType.BLAZE, Material.getMaterial(383), 1, (short) 61, true, "Blaze"),
    CAVESPIDER(PetType.CAVESPIDER, Material.getMaterial(383), 1, (short) 59, true, "Cave Spider"),
    CHICKEN(PetType.CHICKEN, Material.getMaterial(383), 1, (short) 93, true, "Chicken"),
    COW(PetType.COW, Material.getMaterial(383), 1, (short) 92, true, "Cow Pet"),
    CREEPER(PetType.CREEPER, Material.getMaterial(383), 1, (short) 50, true, "Creeper"),
    ENDERDRAGON(PetType.ENDERDRAGON, Material.getMaterial(122), 1, (short) 0, true, "EnderDragon"),
    ENDERMAN(PetType.ENDERMAN, Material.getMaterial(383), 1, (short) 58, true, "Enderman"),
    GHAST(PetType.GHAST, Material.getMaterial(383), 1, (short) 56, true, "Ghast"),
    GIANT(PetType.GIANT, Material.getMaterial(383), 1, (short) 54, true, "Giant"),
    HORSE(PetType.HORSE, Material.getMaterial(383), 1, (short) 100, true, "Horse"),
    IRONGOLEM(PetType.IRONGOLEM, Material.getMaterial(86), 1, (short) 0, true, "Iron Golem"),
    MAGMACUBE(PetType.MAGMACUBE, Material.getMaterial(383), 1, (short) 62, true, "Magma Cube"),
    MUSHROOMCOW(PetType.MUSHROOMCOW, Material.getMaterial(383), 1, (short) 96, true, "Mooshroom"),
    OCELOT(PetType.OCELOT, Material.getMaterial(383), 1, (short) 98, true, "Ocelot"),
    PIG(PetType.PIG, Material.getMaterial(383), 1, (short) 90, true, "Pig"),
    PIGZOMBIE(PetType.PIGZOMBIE, Material.getMaterial(383), 1, (short) 57, true, "Zombie Pigman"),
    SHEEP(PetType.SHEEP, Material.getMaterial(383), 1, (short) 91, true, "Sheep"),
    SILVERFISH(PetType.SILVERFISH, Material.getMaterial(383), 1, (short) 60, true, "Silverfish"),
    SKELETON(PetType.SKELETON, Material.getMaterial(383), 1, (short) 51, true, "Archer Skeleton"),
    SLIME(PetType.SLIME, Material.getMaterial(383), 1, (short) 55, true, "Slime"),
    SPIDER(PetType.SPIDER, Material.getMaterial(383), 1, (short) 52, true, "Spider"),
    SQUID(PetType.SQUID, Material.getMaterial(383), 1, (short) 94, true, "Squid"),
    VILLAGER(PetType.VILLAGER, Material.getMaterial(383), 1, (short) 120, true, "Villager"),
    WITCH(PetType.WITCH, Material.getMaterial(383), 1, (short) 66, true, "Witch"),
    WITHER(PetType.WITHER, Material.getMaterial(399), 1, (short) 0, true, "Wither"),
    WOLF(PetType.WOLF, Material.getMaterial(383), 1, (short) 95, true, "Wolf"),
    ZOMBIE(PetType.ZOMBIE, Material.getMaterial(383), 1, (short) 54, true, "Zombie");

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
        return i;
    }
}
