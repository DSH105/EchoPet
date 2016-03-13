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

package com.dsh105.echopet.compat.api.util.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.inventory.MenuIcon;

public class SelectorIcon extends MenuIcon {

    private String command;
    private PetType petType;

	public SelectorIcon(int slot, String command, PetType petType, Material material, int materialData, String name, String... lore){
		super(slot, material, materialData, name, lore);
        this.command = command;
        this.petType = petType;
    }

	public SelectorIcon(int slot, String command, PetType petType, Material material, String entityTag, String name, String... lore){
		super(slot, material, entityTag, name, lore);
		this.command = command;
		this.petType = petType;
	}

    public String getCommand() {
        return command;
    }

    public PetType getPetType() {
        return petType;
    }


    public ItemStack getIcon(Player viewer) {
        ItemStack i = super.getIcon(viewer);
        ItemMeta meta = i.getItemMeta();
		ChatColor c = this.petType == null ? ChatColor.YELLOW : (viewer.hasPermission("echopet.pet.type." + this.getPetType().toString().toLowerCase().replace("_", ""))) ? ChatColor.GREEN : ChatColor.RED;
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', c + this.getName()));// NPE
        i.setItemMeta(meta);

        if (this.petType == PetType.HUMAN && i.getItemMeta() instanceof SkullMeta) {
            SkullMeta sm = (SkullMeta) i.getItemMeta();
            sm.setOwner(viewer.getName());
            i.setItemMeta(sm);
		}
        return i;
    }


    public void onClick(final Player viewer) {
        viewer.closeInventory();
        if (this.command.equalsIgnoreCase(EchoPet.getPlugin().getCommandString() + " menu")) {
            new BukkitRunnable() {

                public void run() {
                    viewer.performCommand(getCommand());

                }
            }.runTaskLater(EchoPet.getPlugin(), 5L);
        } else {
            viewer.performCommand(this.getCommand());
        }
    }
}