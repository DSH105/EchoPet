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

package io.github.dsh105.echopet.compat.api.config;

import com.dsh105.dshutils.inventory.MenuIcon;
import io.github.dsh105.echopet.compat.api.entity.PetType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class SelectorIcon extends MenuIcon {

    private String command;
    private PetType petType;

    public SelectorIcon(int slot, String command, PetType petType, int materialId, int materialData, String name, String... lore) {
        super(slot, materialId, materialData, name, lore);
        this.command = command;
        this.petType = petType;
    }

    public String getCommand() {
        return command;
    }

    public PetType getPetType() {
        return petType;
    }

    @Override
    public ItemStack getIcon(Player viewer) {
        ItemStack i = super.getIcon(viewer);
        ItemMeta meta = i.getItemMeta();
        ChatColor c = this.petType == null ? ChatColor.YELLOW : (viewer.hasPermission("echopet.pet.type." + this.getPetType().toString().toLowerCase().replace("_", ""))) ? ChatColor.GREEN : ChatColor.RED;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', c + this.getName()));
        i.setItemMeta(meta);

        if (this.petType == PetType.HUMAN && i.getItemMeta() instanceof SkullMeta) {
            SkullMeta sm = (SkullMeta) i.getItemMeta();
            sm.setOwner(viewer.getName());
            i.setItemMeta(sm);
        }
        return i;
    }

    @Override
    public void onClick(Player viewer) {
        viewer.closeInventory();
        viewer.performCommand(this.getCommand());
    }
}