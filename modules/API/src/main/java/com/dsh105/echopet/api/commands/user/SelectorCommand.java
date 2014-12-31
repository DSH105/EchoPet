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

package com.dsh105.echopet.api.commands.user;

import com.dsh105.echopet.api.commands.influx.EchoPetCommandEvent;
import com.dsh105.echopet.api.configuration.Lang;
import com.dsh105.echopet.api.inventory.PetSelector;
import com.dsh105.echopet.bridge.container.PlayerCommandSourceContainer;
import com.dsh105.echopet.util.Perm;
import com.dsh105.influx.CommandListener;
import com.dsh105.influx.annotation.Authorize;
import com.dsh105.influx.annotation.Command;

public class SelectorCommand implements CommandListener {

    @Command(
            syntax = "select",
            desc = "Open the pet selection menu to create a new pet"
    )
    @Authorize(Perm.SELECT)
    public boolean select(EchoPetCommandEvent<PlayerCommandSourceContainer> event) {
        PetSelector.getInventory().show(event.sender().get());
        return true;
    }

    @Command(
            syntax = "selector",
            desc = "Transfers the Pet Selection Menu item to your inventory",
            help = "This item can be used to open the Pet Selection Menu"
    )
    @Authorize(Perm.SELECTOR)
    public boolean selector(EchoPetCommandEvent<PlayerCommandSourceContainer> event) {
        // TODO
        event.sender().getInventory().addItem(PetSelector.getInventory().getInteractIcon().asBukkit());
        event.respond(Lang.SELECTOR_ITEM_ADDED.getValue());
        return true;
    }
}