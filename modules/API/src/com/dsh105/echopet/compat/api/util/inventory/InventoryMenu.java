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

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.dsh105.echopet.compat.api.plugin.EchoPet;

public class InventoryMenu implements InventoryHolder, Listener {

    private int size;
    private String title;
    private HashMap<Integer, MenuIcon> slots = new HashMap<Integer, MenuIcon>();

    protected InventoryMenu(String title, int size) {
        EchoPet.getPlugin().getServer().getPluginManager().registerEvents(this, EchoPet.getPlugin());

        if (size < 0) {
            size = 9;
        } else if (size % 9 != 0) {
            size += 9 - (size % 9);
        }
        this.title = title;
        this.size = size;
    }


    public Inventory getInventory() {
        return Bukkit.createInventory(this, size, title);
    }

    public InventoryMenu showTo(Player viewer) {
        Inventory inv = this.getInventory();
        for (Map.Entry<Integer, MenuIcon> entry : slots.entrySet()) {
            inv.setItem(entry.getKey(), entry.getValue().getIcon(viewer));
        }
        viewer.openInventory(inv);
        return this;
    }

    public int getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }

    public HashMap<Integer, MenuIcon> getSlots() {
        return slots;
    }

    public InventoryMenu setSlot(int slot, MenuIcon icon) {
        this.slots.put(slot, icon);
        return this;
    }

    public MenuIcon getSlot(int slot) {
        return this.slots.get(slot);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        HumanEntity human = event.getWhoClicked();
        if (human instanceof Player) {
            Player player = (Player) human;
            Inventory inv = event.getView().getTopInventory();
            if (inv.getHolder() != null && inv.getHolder() instanceof InventoryMenu && event.getRawSlot() >= 0 && event.getRawSlot() < size) {
                InventoryMenu menu = (InventoryMenu) inv.getHolder();
                event.setCancelled(true);
                MenuIcon icon = slots.get(event.getSlot());
                if (icon != null) {
                    icon.onClick(player);
                    player.closeInventory();
                }
            }
        }
    }
}