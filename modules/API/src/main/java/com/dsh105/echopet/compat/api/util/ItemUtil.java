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

package com.dsh105.echopet.compat.api.util;

import org.bukkit.inventory.ItemStack;

public class ItemUtil {

    public static boolean matches(ItemStack itemStack1, ItemStack itemStack2) {
        if (itemStack1 == null || itemStack2 == null) {
            return false;
        }

        if (!itemStack1.getType().equals(itemStack2.getType())) {
            return false;
        }

        if (itemStack1.getAmount() != itemStack2.getAmount()) {
            return false;
        }

        if (itemStack1.getDurability() != itemStack2.getDurability()) {
            return false;
        }

        if (itemStack1.getMaxStackSize() != itemStack2.getMaxStackSize()) {
            return false;
        }

        if (itemStack1.hasItemMeta() != itemStack2.hasItemMeta()) {
            return false;
        }

        if (itemStack1.hasItemMeta() && itemStack2.hasItemMeta() && !itemStack1.getItemMeta().equals(itemStack2.getItemMeta())) {
            return false;
        }

        return true;
    }

}
