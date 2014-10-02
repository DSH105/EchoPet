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

/*
 * This file is part of HoloAPI.
 *
 * HoloAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HoloAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HoloAPI.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.commands.basic;

import com.dsh105.influx.CommandListener;
import com.dsh105.influx.Controller;
import com.dsh105.influx.annotation.Bind;
import com.dsh105.influx.annotation.Command;
import com.dsh105.influx.dispatch.BukkitCommandEvent;
import org.bukkit.command.CommandSender;

import java.util.SortedMap;

public class HelpCommand implements CommandListener {

    @Command(
            syntax = "help",
            aliases = {"?"},
            desc = "Retrieve help for all EchoPet commands",
            help = {
                    "Commands are listed in alphabetical order",
                    "Use \"/pet help <index>\" for a certain page of help",
                    "Use \"/pet help <command>\" for more help on a certain command"
            }
    )
    public boolean help(BukkitCommandEvent event) {
        event.getManager().getHelp().sendPage(event.sender());
        return true;
    }

    @Command(
            syntax = "help <index>",
            aliases = {"? <index>"},
            desc = "Retrieve a certain help page of all EchoPet commands",
            help = {
                    "Commands are listed in alphabetical order",
                    "Use \"/pet help <index>\" for a certain page of help",
                    "Use \"/pet help <command>\" for more help on a certain command"
            }
    )
    public boolean helpPage(BukkitCommandEvent event, @Bind("index") int index) {
        event.getManager().getHelp().sendPage(event.sender(), index);
        return true;
    }

    @Command(
            syntax = "help <command>",
            aliases = {"? <command>"},
            desc = "Retrieve help on a certain EchoPet command",
            help = {
                    "Commands are listed in alphabetical order",
                    "Use \"/pet help\" for general help and a command listing",
                    "Use \"/pet help <index>\" for a certain page of help"
            }
    )
    public boolean commandHelp(BukkitCommandEvent<CommandSender> event) {
        String command = event.getInput();
        SortedMap<Controller, String[]> matches = event.getManager().getHelp().getHelpFor(command);
        if (matches.isEmpty()) {
            event.respond("No help found for \"" + command + "\".");
            return true;
        }

        event.respond(matches.size() + " matches found for \"" + command + "\":");

        for (Controller controller : matches.keySet()) {
            System.out.println('\n');
            event.getManager().getHelp().sendHelpFor(event.sender(), controller);
        }
        return true;
    }
}
