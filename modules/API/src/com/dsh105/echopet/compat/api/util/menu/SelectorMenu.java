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

import com.dsh105.echopet.compat.api.config.ConfigOptions;
import com.dsh105.echopet.compat.api.event.PetMenuOpenEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.inventory.InventoryMenu;
import org.bukkit.entity.Player;

import java.util.Map;

public class SelectorMenu extends InventoryMenu {

    SelectorMenu() {
        super(ConfigOptions.instance.getConfig().getString("petSelector.menu.title", "Pets"), ConfigOptions.instance.getConfig().getInt("petSelector.menu.slots", 45));
        for (Map.Entry<Integer, SelectorIcon> entry : SelectorLayout.getLoadedLayout().entrySet()) {
            this.setSlot(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public InventoryMenu showTo(Player viewer) {
        PetMenuOpenEvent menuEvent = new PetMenuOpenEvent(viewer, PetMenuOpenEvent.MenuType.SELECTOR);
        EchoPet.getPlugin().getServer().getPluginManager().callEvent(menuEvent);
        if (menuEvent.isCancelled()) {
            return this;
        }
        return super.showTo(viewer);
    }
}