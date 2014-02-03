package io.github.dsh105.echopet.menu.main;

import io.github.dsh105.echopet.menu.main.DataMenu.DataMenuType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum MenuItem {

    HORSE_TYPE(Material.HAY_BLOCK, 1, (short) 0, DataMenuType.HORSE_TYPE, "Type", "Horse"),
    HORSE_VARIANT(Material.LEASH, 1, (short) 0, DataMenuType.HORSE_VARIANT, "Variant", "Horse"),
    HORSE_MARKING(Material.INK_SACK, 1, (short) 0, DataMenuType.HORSE_MARKING, "Marking", "Horse"),
    HORSE_ARMOUR(Material.IRON_CHESTPLATE, 1, (short) 0, DataMenuType.HORSE_ARMOUR, "Armour", "Horse"),
    CHESTED(Material.CHEST, 1, (short) 0, DataMenuType.BOOLEAN, "Chested", "Horse"),
    FIRE(Material.FIRE, 1, (short) 0, DataMenuType.BOOLEAN, "Fire", "Blaze"),
    SADDLE(Material.SADDLE, 1, (short) 0, DataMenuType.BOOLEAN, "Saddle", "Horse", "Pig"),
    SHEARED(Material.SHEARS, 1, (short) 0, DataMenuType.BOOLEAN, "Sheared", "Sheep"),
    SCREAMING(Material.ENDER_PEARL, 1, (short) 0, DataMenuType.BOOLEAN, "Screaming", "Enderman"),
    POTION(Material.getMaterial(373), 1, (short) 0, DataMenuType.BOOLEAN, "Potion", "Witch"),
    SHIELD(Material.GLASS, 1, (short) 0, DataMenuType.BOOLEAN, "Shield", "Wither"),
    POWER(Material.BEACON, 1, (short) 0, DataMenuType.BOOLEAN, "Powered", "Creeper"),
    SIZE(Material.SLIME_BALL, 1, (short) 0, DataMenuType.SIZE, "Size", "Slime", "MagmaCube"),
    BABY(Material.WHEAT, 1, (short) 0, DataMenuType.BOOLEAN, "Baby", "PigZombie", "Zombie", "Chicken", "Cow", "Horse", "MushroomCow", "Ocelot", "Pig", "Sheep", "Wolf", "Villager"),
    CAT_TYPE(Material.RAW_FISH, 1, (short) 0, DataMenuType.CAT_TYPE, "Cat Type", "Ocelot"),
    ANGRY(Material.BONE, 1, (short) 0, DataMenuType.BOOLEAN, "Angry", "Wolf"),
    TAMED(Material.BONE, 1, (short) 0, DataMenuType.BOOLEAN, "Tamed", "Wolf"),
    WITHER(Material.getMaterial(397), 1, (short) 1, DataMenuType.BOOLEAN, "Wither", "Skeleton"),
    VILLAGER(Material.EMERALD, 1, (short) 0, DataMenuType.BOOLEAN, "Villager", "Zombie", "PigZombie"),
    COLOR(Material.WOOL, 1, (short) 0, DataMenuType.COLOR, "Color", "Sheep", "Wolf"),
    PROFESSION(Material.IRON_AXE, 1, (short) 0, DataMenuType.PROFESSION, "Profession", "Villager"),
    RIDE(Material.CARROT_STICK, 1, (short) 0, DataMenuType.BOOLEAN, "Ride Pet", "Control your pet."),
    HAT(Material.IRON_HELMET, 1, (short) 0, DataMenuType.BOOLEAN, "Hat Pet", "Wear your pet on your head.");

    private Material mat;
    private String name;
    private int amount;
    private List<String> lore;
    private short data;
    DataMenuType menuType;

    MenuItem(Material mat, int amount, short data, DataMenuType menuType, String name, String... lore) {
        this.mat = mat;
        this.name = name;
        this.amount = amount;
        this.data = data;
        List<String> list = new ArrayList<String>();
        list.addAll(Arrays.asList(lore));
        this.lore = list;
        this.menuType = menuType;
    }

    public ItemStack getItem() {
        ItemStack i = new ItemStack(this.mat, this.amount, this.data);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(ChatColor.RED + this.name);
        meta.setLore(this.lore);
        i.setItemMeta(meta);
        return i;
    }

    public ItemStack getBoolean(boolean flag) {
        ItemStack i = new ItemStack(this.mat, this.amount, this.data);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(ChatColor.RED + this.name + (flag ? ChatColor.GREEN + " [TOGGLE ON]" : ChatColor.YELLOW + " [TOGGLE OFF]"));
        meta.setLore(this.lore);
        i.setItemMeta(meta);
        return i;
    }

    public DataMenuType getMenuType() {
        return this.menuType;
    }
}