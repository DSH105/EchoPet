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
import com.dsh105.echopet.api.inventory.PetSelector;
import com.dsh105.echopet.util.Perm;
import org.bukkit.entity.Player;

public class SelectCommand implements CommandListener {

    @Command(
            command = "select",
            description = "Open the pet selection menu to create a new pet",
            permission = Perm.SELECT
    )
    public boolean command(CommandEvent<Player> event) {
        PetSelector.prepare().show(event.sender());
        return true;
    }
}