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

import com.dsh105.echopet.api.config.ConfigType;
import com.dsh105.echopet.api.config.Lang;
import com.dsh105.echopet.api.inventory.DataMenu;
import com.dsh105.echopet.api.inventory.PetSelector;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.util.Perm;
import com.dsh105.influx.CommandListener;
import com.dsh105.influx.annotation.Authorize;
import com.dsh105.influx.annotation.Command;
import com.dsh105.influx.dispatch.BukkitCommandEvent;

public class ReloadCommand implements CommandListener {

    @Command(
            syntax = "reload",
            desc = "Reload all EchoPet configuration files",
            help = {"This does NOT refresh any pets", "This might not have an effect on certain configuration options - those will require a server restart"}
    )
    @Authorize(Perm.RELOAD)
    public boolean command(BukkitCommandEvent event) {
        EchoPet.getConfig(ConfigType.GENERAL).reloadConfig();
        EchoPet.getConfig(ConfigType.MESSAGES).reloadConfig();
        EchoPet.getConfig(ConfigType.MENU).reloadConfig();
        EchoPet.getConfig(ConfigType.PETS).reloadConfig();
        PetSelector.reloadLayout();
        DataMenu.reloadLayouts();

        event.respond(Lang.CONFIGS_RELOADED.getValue());
        return true;
    }
}