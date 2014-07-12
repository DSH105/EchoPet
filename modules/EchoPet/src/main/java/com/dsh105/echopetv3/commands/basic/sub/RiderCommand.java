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

package com.dsh105.echopetv3.commands.basic.sub;

import com.dsh105.command.Command;
import com.dsh105.command.CommandEvent;
import com.dsh105.command.CommandListener;
import org.bukkit.entity.Player;

public class RiderCommand implements CommandListener {

    @Command(
            command = "<pet_name> rider <type> [name] [data]",
            description = "Creates a rider for an existing pet (specific by <pet_name>",
            permission = "echopet.pet.type.<type>",
            help = {"<pet_name> is the name of an existing pet", "Data values can be separated by a space", "e.g. blue,baby (for a sheep)", "If no pet name is provided, a default will be assigned", "Pet names can also be set using the \"name\" command"}
    )
    public boolean create(CommandEvent<Player> event) {

    }
}