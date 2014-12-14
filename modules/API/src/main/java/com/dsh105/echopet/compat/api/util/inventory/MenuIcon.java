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

package com.dsh105.echopet.compat.api.util.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class MenuIcon {

    private int slot;
    private int materialId;
    private int materialData;
    private String name;
    private String[] lore;

    public MenuIcon(int slot, int materialId, int materialData, String name, String... lore) {
        this.slot = slot;
        this.materialId = materialId;
        this.materialData = materialData;
        this.name = name;
        this.lore = lore;
    }

    public int getSlot() {
        return slot;
    }

    public int getMaterialId() {
        return materialId;
    }

    public int getMaterialData() {
        return materialData;
    }

    public String getName() {
        return name;
    }

    public String[] getLore() {
        return lore;
    }

    public ItemStack getIcon(Player viewer) {
        ItemStack i = new ItemStack(materialId, 1, (short) materialData);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(name);
        if (this.lore != null && this.lore.length > 0) {
            meta.setLore(Arrays.asList(lore));
        }
        i.setItemMeta(meta);
        return i;
    }

    public void onClick(Player viewer) {

    }
}