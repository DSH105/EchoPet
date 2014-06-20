/*
 * This file is part of EchoPet.
 *
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.api.inventory;

import com.dsh105.echopet.util.menu.DataMenu.DataMenuType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum DataPreset {

    HORSE_TYPE(Material.HAY_BLOCK, 1, (short) 0, DataMenuType.HORSE_TYPE, "Type"),
    HORSE_VARIANT(Material.LEASH, 1, (short) 0, DataMenuType.HORSE_VARIANT, "Variant"),
    HORSE_MARKING(Material.INK_SACK, 1, (short) 0, DataMenuType.HORSE_MARKING, "Marking"),
    HORSE_ARMOUR(Material.IRON_CHESTPLATE, 1, (short) 0, DataMenuType.HORSE_ARMOUR, "Armour"),
    CHESTED(Material.CHEST, 1, (short) 0, DataMenuType.BOOLEAN, "Chested"),
    FIRE(Material.FIRE, 1, (short) 0, DataMenuType.BOOLEAN, "Fire"),
    SADDLE(Material.SADDLE, 1, (short) 0, DataMenuType.BOOLEAN, "Saddle"),
    SHEARED(Material.SHEARS, 1, (short) 0, DataMenuType.BOOLEAN, "Sheared"),
    SCREAMING(Material.ENDER_PEARL, 1, (short) 0, DataMenuType.BOOLEAN, "Screaming"),
    POTION(Material.POTION, 1, (short) 0, DataMenuType.BOOLEAN, "Potion"),
    SHIELD(Material.GLASS, 1, (short) 0, DataMenuType.BOOLEAN, "Shield"),
    POWER(Material.BEACON, 1, (short) 0, DataMenuType.BOOLEAN, "Powered"),
    SIZE(Material.SLIME_BALL, 1, (short) 0, DataMenuType.SIZE, "Size"),
    BABY(Material.WHEAT, 1, (short) 0, DataMenuType.BOOLEAN, "Baby"),
    CAT_TYPE(Material.RAW_FISH, 1, (short) 0, DataMenuType.CAT_TYPE, "Cat Type"),
    ANGRY(Material.BONE, 1, (short) 0, DataMenuType.BOOLEAN, "Angry"),
    TAMED(Material.BONE, 1, (short) 0, DataMenuType.BOOLEAN, "Tamed"),
    WITHER(Material.getMaterial(397), 1, (short) 1, DataMenuType.BOOLEAN, "Wither"),
    VILLAGER(Material.EMERALD, 1, (short) 0, DataMenuType.BOOLEAN, "Villager"),
    COLOR(Material.WOOL, 1, (short) 0, DataMenuType.COLOR, "Color", "Sheep"),
    PROFESSION(Material.IRON_AXE, 1, (short) 0, DataMenuType.PROFESSION, "Profession"),
    RIDE(Material.CARROT_STICK, 1, (short) 0, DataMenuType.BOOLEAN, "Ride Pet"),
    HAT(Material.IRON_HELMET, 1, (short) 0, DataMenuType.BOOLEAN, "Hat Pet");

    private Material mat;
    private String name;
    private int amount;
    private List<String> lore;
    private short data;
    DataMenuType menuType;

    DataPreset(Material mat, int amount, short data, DataMenuType menuType, String name, String... lore) {
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