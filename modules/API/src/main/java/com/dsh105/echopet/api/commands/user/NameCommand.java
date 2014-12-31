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
import com.dsh105.echopet.bridge.container.PlayerCommandSourceContainer;
import com.dsh105.echopet.util.Perm;
import com.dsh105.influx.CommandListener;
import com.dsh105.influx.annotation.Authorize;
import com.dsh105.influx.annotation.Command;
import com.dsh105.influx.annotation.Convert;

public class NameCommand implements CommandListener {

    @Command(
            syntax = "name [name]",
            desc = "Sets the name of your currently selected pet",
            help = {"Use \"/pet view\" to select a pet to edit.", "If you only have one pet, there is no need to select one to edit.", "If a name is not provided in the command, you will be asked to enter a name separately", "Names can be more than one word if enclosed in single or double quotations e.g. sheep \"name:My cool pet\""}
    )
    @Authorize(Perm.NAME)
    public boolean name(EchoPetCommandEvent<PlayerCommandSourceContainer> event, @Convert(PetConverters.Selected.class) Pet pet) {
        if (pet == null) {
            return true;
        }

        String name = event.var("name");
        if (name == null) {
            // TODO: ask for name input
            //NameFactory.askForName(event.sender(), pet, false);
        } else {
            pet.setName(name, true);
            event.respond(Lang.NAME_PET.getValue("name", pet.getName(), "newname", name));
        }
        return true;
    }
}