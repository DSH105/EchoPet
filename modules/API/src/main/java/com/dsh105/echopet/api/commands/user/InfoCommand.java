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

import com.dsh105.echopet.api.commands.PetConverters;
import com.dsh105.echopet.api.commands.influx.EchoPetCommandEvent;
import com.dsh105.echopet.api.configuration.Lang;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.inventory.ViewMenu;
import com.dsh105.echopet.bridge.container.PlayerCommandSourceContainer;
import com.dsh105.echopet.util.Perm;
import com.dsh105.influx.CommandListener;
import com.dsh105.influx.annotation.Authorize;
import com.dsh105.influx.annotation.Command;
import com.dsh105.influx.annotation.Convert;
import com.dsh105.powermessage.core.PowerMessage;
import org.bukkit.ChatColor;

public class InfoCommand implements CommandListener {

    @Command(
            syntax = "info",
            desc = "Retrieve info on your currently selected pet.",
            help = {"Use \"/pet view\" to select a pet to edit.", "If you only have one pet, there is no need to select one to edit."}
    )
    @Authorize(Perm.INFO)
    public boolean infoForPet(EchoPetCommandEvent<PlayerCommandSourceContainer> event, @Convert(PetConverters.Selected.class) Pet pet) {
        if (pet == null) {
            return true;
        }

        // TODO
        new PowerMessage(ChatColor.WHITE + "• {c1}" + pet.getType().humanName() + " ({c2}" + pet.getName() + "{c1})")
                .tooltip(ViewMenu.getHoverInfo(pet))
                .send(event.sender());
        event.respond(Lang.HOVER_TIP.getValue());
        return true;
    }
}