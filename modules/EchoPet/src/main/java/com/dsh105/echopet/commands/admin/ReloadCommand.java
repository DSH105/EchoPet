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

package com.dsh105.echopet.commands.admin;

import com.dsh105.command.Command;
import com.dsh105.command.CommandEvent;
import com.dsh105.command.CommandListener;
import com.dsh105.echopet.api.config.ConfigType;
import com.dsh105.echopet.api.config.Lang;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.util.Perm;

public class ReloadCommand implements CommandListener {

    @Command(
            command = "reload",
            description = "Reload all EchoPet configuration files",
            permission = Perm.RELOAD,
            help = {"This does NOT refresh any pets", "This might not have an effect on certain configuration options - those will require a server restart"}
    )
    public boolean command(CommandEvent event) {
        EchoPet.getConfig(ConfigType.GENERAL).reloadConfig();
        EchoPet.getConfig(ConfigType.MESSAGES).reloadConfig();
        EchoPet.getConfig(ConfigType.MENU).reloadConfig();
        EchoPet.getConfig(ConfigType.PETS).reloadConfig();

        event.respond(Lang.CONFIGS_RELOADED.getValue());
        return true;
    }
}