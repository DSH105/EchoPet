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

package com.dsh105.echopetv3.commands.basic;

import com.captainbern.minecraft.reflection.MinecraftReflection;
import com.dsh105.command.Command;
import com.dsh105.command.CommandEvent;
import com.dsh105.command.CommandListener;
import com.dsh105.commodus.GeneralUtil;
import com.dsh105.echopetv3.api.config.Lang;
import com.dsh105.echopetv3.api.plugin.EchoPet;
import com.dsh105.echopetv3.util.Perm;
import org.bukkit.entity.Player;

public class HelpCommand implements CommandListener {

    @Command(
            command = "help",
            description = "Retrieve help for all EchoPet commands",
            permission = Perm.PET
    )
    public boolean help(CommandEvent event) {
        EchoPet.getCommandManager().getHelpService().sendPage(event.sender(), 1);
        if (MinecraftReflection.isUsingNetty() && event.sender() instanceof Player) {
            event.respond(Lang.COMMAND_HOVER_TIP.getValue());
        }
        return true;
    }

    @Command(
            command = "help <index>",
            description = "Retrieve a certain help page of all EchoPet commands",
            permission = Perm.PET
    )
    public boolean helpPage(CommandEvent event) {
        try {
            EchoPet.getCommandManager().getHelpService().sendPage(event.sender(), GeneralUtil.toInteger(event.variable("index")));
            if (MinecraftReflection.isUsingNetty() && event.sender() instanceof Player) {
                event.respond(Lang.COMMAND_HOVER_TIP.getValue());
            }
        } catch (NumberFormatException e) {
            event.respond(Lang.HELP_INDEX_TOO_BIG.getValue("index", event.variable("index")));
        }
        return true;
    }
}