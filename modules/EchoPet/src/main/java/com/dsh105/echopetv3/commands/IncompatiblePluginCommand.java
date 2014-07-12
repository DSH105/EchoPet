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

package com.dsh105.echopetv3.commands;

import com.dsh105.command.Command;
import com.dsh105.command.CommandEvent;
import com.dsh105.command.CommandListener;
import com.dsh105.command.ResponseLevel;
import com.dsh105.echopetv3.util.Perm;

public class IncompatiblePluginCommand implements CommandListener {

    @Command(
            command = "pet",
            description = "Manage your own pets",
            permission = Perm.PET,
            usage = "Use \"/pet help\" for help."
    )
    public boolean pet(CommandEvent event) {
        event.respond(ResponseLevel.SEVERE, "EchoPet is not compatible with this server version. Please upgrade/downgrade to the appropriate version.");
        return true;
    }

    @Command(
            command = "petadmin",
            description = "Admin access to pet management",
            permission = Perm.PETADMIN,
            usage = "Use \"/petadmin help\" for help."
    )
    public boolean petadmin(CommandEvent event) {
        event.respond(ResponseLevel.SEVERE, "EchoPet is not compatible with this server version. Please upgrade/downgrade to the appropriate version.");
        return true;
    }
}