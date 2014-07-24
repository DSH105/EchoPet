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

package com.dsh105.echopet.commands.basic;

import com.dsh105.command.Command;
import com.dsh105.command.CommandEvent;
import com.dsh105.command.CommandListener;
import com.dsh105.echopet.api.config.Lang;
import com.dsh105.echopet.api.inventory.PetSelector;
import com.dsh105.echopet.util.Perm;
import org.bukkit.entity.Player;

public class SelectorCommand implements CommandListener {

    @Command(
            command = "select",
            description = "Transfers the Pet Selection Menu item to your inventory",
            permission = Perm.SELECTOR,
            help = "This item can be used to open the Pet Selection Menu"
    )
    public boolean command(CommandEvent<Player> event) {
        event.sender().getInventory().addItem(PetSelector.getLayout().getClickItem());
        event.respond(Lang.SELECTOR_ITEM_ADDED.getValue());
        return true;
    }
}