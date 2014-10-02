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

package com.dsh105.echopet.commands;

import com.dsh105.echopet.util.Perm;
import com.dsh105.influx.CommandListener;
import com.dsh105.influx.annotation.Authorize;
import com.dsh105.influx.annotation.Command;
import com.dsh105.influx.dispatch.BukkitCommandEvent;
import com.dsh105.influx.response.ResponseLevel;

public class IncompatiblePluginCommand implements CommandListener {

    @Command(
            syntax = "pet [args...]",
            desc = "Manage your own pets",
            usage = "Use \"/pet help\" for help."
    )
    @Authorize(Perm.PET)
    public boolean pet(BukkitCommandEvent event) {
        event.respond("EchoPet is not compatible with this server version. Please upgrade/downgrade to the appropriate version.", ResponseLevel.SEVERE);
        return true;
    }

    @Command(
            syntax = "petadmin [args...]",
            desc = "Admin access to pet management",
            usage = "Use \"/petadmin help\" for help."
    )
    @Authorize(Perm.PETADMIN)
    public boolean petadmin(BukkitCommandEvent event) {
        event.respond("EchoPet is not compatible with this server version. Please upgrade/downgrade to the appropriate version.", ResponseLevel.SEVERE);
        return true;
    }
}