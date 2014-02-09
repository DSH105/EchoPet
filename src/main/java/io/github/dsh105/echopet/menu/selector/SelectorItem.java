package io.github.dsh105.echopet.menu.selector;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public enum SelectorItem {

    SELECTOR(Material.BONE, 1, (short) 0, ChatColor.GREEN + "Pets", ""),
    TOGGLE(Material.BONE, 1, (short) 0, ChatColor.YELLOW + "Toggle Pet", "toggle"),
    CALL(Material.ENDER_PEARL, 1, (short) 0, ChatColor.YELLOW + "Call Pet", "call"),
    RIDE(Material.CARROT_STICK, 1, (short) 0, ChatColor.YELLOW + "Ride Pet", "ride"),
    HAT(Material.IRON_HELMET, 1, (short) 0, ChatColor.YELLOW + "Hat Pet", "hat"),
    MENU(Material.WORKBENCH, 1, (short) 0, ChatColor.YELLOW + "Open PetMenu", "menu"),
    CLOSE(Material.BOOK, 1, (short) 0, ChatColor.YELLOW + "Close", "select");

    private String command;
    private Material mat;
    private int amount;
    private short data;
    private String name;

    SelectorItem(Material mat, int amount, short data, String name, String command) {
        this.command = "pet " + command;
        this.mat = mat;
        this.amount = amount;
        this.data = data;
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public Material getMat() {
        return mat;
    }

    public int getAmount() {
        return amount;
    }

    public short getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItem() {
        ItemStack i = new ItemStack(this.mat, this.amount, this.data);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(this.name);
        //meta.setLore(this.lore);
        i.setItemMeta(meta);
        return i;
    }

    public ItemStack getItem(int amount) {
        ItemStack i = new ItemStack(this.mat, amount, this.data);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(this.name);
        //meta.setLore(this.lore);
        i.setItemMeta(meta);
        return i;
    }
}