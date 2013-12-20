package io.github.dsh105.echopet.menu.main;

import io.github.dsh105.echopet.entity.living.PetData;
import io.github.dsh105.echopet.menu.main.DataMenu.DataMenuType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public enum DataMenuItem {
    BOOLEAN_TRUE(DataMenuType.BOOLEAN, null, Material.getMaterial(76), 1, (short) 0, "True", "Turns the feature on."),
    BOOLEAN_FALSE(DataMenuType.BOOLEAN, null, Material.getMaterial(75), 1, (short) 0, "False", "Turns the feature off."),

    BLACK_CAT(DataMenuType.CAT_TYPE, PetData.BLACK, Material.getMaterial(351), 1, (short) 0, "Black", "Cat Type"),
    RED_CAT(DataMenuType.CAT_TYPE, PetData.RED, Material.getMaterial(351), 1, (short) 1, "Red", "Cat Type"),
    SIAMESE_CAT(DataMenuType.CAT_TYPE, PetData.SIAMESE, Material.getMaterial(351), 1, (short) 7, "Siamese", "Cat Type"),
    WILD_CAT(DataMenuType.CAT_TYPE, PetData.WILD, Material.getMaterial(351), 1, (short) 14, "Wild", "Cat Type"),

    SMALL(DataMenuType.SIZE, PetData.SMALL, Material.SLIME_BALL, 1, (short) 0, "Small", "Slime Size"),
    MEDIUM(DataMenuType.SIZE, PetData.MEDIUM, Material.SLIME_BALL, 2, (short) 0, "Medium", "Slime Size"),
    LARGE(DataMenuType.SIZE, PetData.LARGE, Material.SLIME_BALL, 4, (short) 0, "Large", "Slime Size"),

    BLACKSMITH(DataMenuType.PROFESSION, PetData.BLACKSMITH, Material.COAL, 1, (short) 0, "Blacksmith", "Villager Profession"),
    BUTCHER(DataMenuType.PROFESSION, PetData.BUTCHER, Material.RAW_BEEF, 1, (short) 0, "Butcher", "Villager Profession"),
    FARMER(DataMenuType.PROFESSION, PetData.FARMER, Material.IRON_HOE, 1, (short) 0, "Farmer", "Villager Profession"),
    LIBRARIAN(DataMenuType.PROFESSION, PetData.LIBRARIAN, Material.BOOK, 1, (short) 0, "Librarian", "Villager Profession"),
    PRIEST(DataMenuType.PROFESSION, PetData.PRIEST, Material.PAPER, 1, (short) 0, "Priest", "Villager Profession"),

    BLACK(DataMenuType.COLOR, PetData.BLACK, Material.WOOL, 1, (short) 15, "Black", "Wool Or Collar Color"),
    BLUE(DataMenuType.COLOR, PetData.BLUE, Material.WOOL, 1, (short) 11, "Blue", "Wool Or Collar Color"),
    BROWN(DataMenuType.COLOR, PetData.BROWN, Material.WOOL, 1, (short) 12, "Brown", "Wool Or Collar Color"),
    CYAN(DataMenuType.COLOR, PetData.CYAN, Material.WOOL, 1, (short) 9, "Cyan", "Wool Or Collar Color"),
    GRAY(DataMenuType.COLOR, PetData.GRAY, Material.WOOL, 1, (short) 7, "Gray", "Wool Or Collar Color"),
    GREEN(DataMenuType.COLOR, PetData.GREEN, Material.WOOL, 1, (short) 13, "Green", "Wool Or Collar Color"),
    LIGHT_BLUE(DataMenuType.COLOR, PetData.LIGHTBLUE, Material.WOOL, 1, (short) 3, "Light Blue", "Wool Or Collar Color"),
    LIME(DataMenuType.COLOR, PetData.LIME, Material.WOOL, 1, (short) 5, "Lime", "Wool Or Collar Color"),
    MAGENTA(DataMenuType.COLOR, PetData.MAGENTA, Material.WOOL, 1, (short) 2, "Magenta", "Wool Or Collar Color"),
    ORANGE(DataMenuType.COLOR, PetData.ORANGE, Material.WOOL, 1, (short) 1, "Orange", "Wool Or Collar Color"),
    PINK(DataMenuType.COLOR, PetData.PINK, Material.WOOL, 1, (short) 6, "Pink", "Wool Or Collar Color"),
    PURPLE(DataMenuType.COLOR, PetData.PURPLE, Material.WOOL, 1, (short) 10, "Purple", "Wool Or Collar Color"),
    RED(DataMenuType.COLOR, PetData.RED, Material.WOOL, 1, (short) 14, "Red", "Wool Or Collar Color"),
    SILVER(DataMenuType.COLOR, PetData.SILVER, Material.WOOL, 1, (short) 8, "Silver", "Wool Or Collar Color"),
    WHITE(DataMenuType.COLOR, PetData.WHITE, Material.WOOL, 1, (short) 0, "White", "Wool Or Collar Color"),
    YELLOW(DataMenuType.COLOR, PetData.YELLOW, Material.WOOL, 1, (short) 4, "Yellow", "Wool Or Collar Color"),

    NORMAL(DataMenuType.HORSE_TYPE, PetData.NORMAL, Material.HAY_BLOCK, 1, (short) 0, "Normal", "Type"),
    DONKEY(DataMenuType.HORSE_TYPE, PetData.DONKEY, Material.CHEST, 1, (short) 0, "Donkey", "Type"),
    MULE(DataMenuType.HORSE_TYPE, PetData.MULE, Material.CHEST, 1, (short) 0, "Mule", "Type"),
    ZOMBIE(DataMenuType.HORSE_TYPE, PetData.ZOMBIE, Material.ROTTEN_FLESH, 1, (short) 0, "Zombie", "Type"),
    SKELETON(DataMenuType.HORSE_TYPE, PetData.SKELETON, Material.BOW, 1, (short) 0, "Skeleton", "Type"),

    WHITE_HORSE(DataMenuType.HORSE_VARIANT, PetData.WHITE, Material.WOOL, 1, (short) 0, "White", "Variant"),
    CREAMY_HORSE(DataMenuType.HORSE_VARIANT, PetData.CREAMY, Material.WOOL, 1, (short) 4, "Creamy", "Variant"),
    CHESTNUT_HORSE(DataMenuType.HORSE_VARIANT, PetData.CHESTNUT, Material.STAINED_CLAY, 1, (short) 8, "Chestnut", "Variant"),
    BROWN_HORSE(DataMenuType.HORSE_VARIANT, PetData.BROWN, Material.WOOL, 1, (short) 12, "Brown", "Variant"),
    BLACK_HORSE(DataMenuType.HORSE_VARIANT, PetData.BLACK, Material.WOOL, 1, (short) 15, "Black", "Variant"),
    GRAY_HORSE(DataMenuType.HORSE_VARIANT, PetData.GRAY, Material.WOOL, 1, (short) 7, "Gray", "Variant"),
    DARKBROWN_HORSE(DataMenuType.HORSE_VARIANT, PetData.DARKBROWN, Material.STAINED_CLAY, 1, (short) 7, "Dark Brown", "Variant"),

    NONE(DataMenuType.HORSE_MARKING, PetData.NONE, Material.LEASH, 1, (short) 0, "None", "Marking"),
    SOCKS(DataMenuType.HORSE_MARKING, PetData.SOCKS, Material.LEASH, 1, (short) 0, "White Socks", "Marking"),
    WHITE_PATCH(DataMenuType.HORSE_MARKING, PetData.WHITEPATCH, Material.LEASH, 1, (short) 0, "White Patch", "Marking"),
    WHITE_SPOTS(DataMenuType.HORSE_MARKING, PetData.WHITESPOT, Material.LEASH, 1, (short) 0, "White Spots", "Marking"),
    BLACK_SPOTS(DataMenuType.HORSE_MARKING, PetData.BLACKSPOT, Material.LEASH, 1, (short) 0, "Black Spots", "Marking"),

    NOARMOUR(DataMenuType.HORSE_ARMOUR, PetData.NOARMOUR, Material.LEASH, 1, (short) 0, "None", "Armour"),
    IRON(DataMenuType.HORSE_ARMOUR, PetData.IRON, Material.getMaterial(417), 1, (short) 0, "Iron", "Armour"),
    GOLD(DataMenuType.HORSE_ARMOUR, PetData.GOLD, Material.getMaterial(418), 1, (short) 0, "Gold", "Armour"),
    DIAMOND(DataMenuType.HORSE_ARMOUR, PetData.DIAMOND, Material.getMaterial(419), 1, (short) 0, "Diamond", "Armour"),

    BACK(DataMenuType.OTHER, null, Material.BOOK, 1, (short) 0, "Back", "Return to the main menu."),
    CLOSE(DataMenuType.OTHER, null, Material.BOOK, 1, (short) 0, "Close", "Close the Pet Menu");

    private DataMenuType type;
    private PetData dataLink;
    private Material mat;
    private String name;
    private List<String> lore;
    private int amount;
    private short data;

    DataMenuItem(DataMenuType type, PetData dataLink, Material mat, int amount, short data, String name, String... lore) {
        this.type = type;
        this.dataLink = dataLink;
        this.mat = mat;
        this.amount = amount;
        this.data = data;
        this.name = name;
        List<String> list = new ArrayList<String>();
        for (String s : lore) {
            s = ChatColor.GOLD + s;
            list.add(s);
        }
        this.lore = list;
    }

    public ItemStack getItem() {
        ItemStack i = new ItemStack(this.mat, this.amount, this.data);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(ChatColor.RED + this.name);
        meta.setLore(this.lore);
        i.setItemMeta(meta);
        return i;
    }

    public DataMenuType getType() {
        return this.type;
    }

    public PetData getDataLink() {
        return this.dataLink;
    }
}