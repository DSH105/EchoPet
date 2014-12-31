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
import com.dsh105.influx.annotation.*;
import com.dsh105.influx.dispatch.BukkitCommandEvent;
import org.bukkit.entity.Player;

public class RiderCommand implements CommandListener {

    @Command(
            syntax = "rider <type>",
            desc = "Creates a rider from the given <type> for your currently selected pet",
            help = {"Use \"/pet view\" to select a pet to edit.", "If you only have one pet, there is no need to select one to edit.", "Data values can be separated by a comma", "e.g. blue,baby (for a sheep)", "Names can be more than one word if enclosed in single or double quotations e.g. sheep \"name:My cool pet\"", "If no pet name is provided, a default will be assigned", "Pet names can also be set using the \"rider name\" command"}
    )
    @Authorize(Perm.RIDER_TYPE)
    public boolean rider(BukkitCommandEvent<Player> event,
                         @Bind("type") @Convert(PetConverters.CreateType.class) Pet rider,
                         @Convert(PetConverters.Selected.class) Pet pet) {
        if (pet != null && rider != null) {
            pet.setRider(rider, true);
        }
        return true;
    }

    @Command(
            syntax = "rider data <data...>",
            desc = "Applies the given data types to the rider of your currently selected pet.",
            help = {"Use \"/pet view\" to select a pet to edit.", "If you only have one pet, there is no need to select one to edit."}
    )
    @Authorize(Perm.DATA)
    @Nested
    public boolean applyData(EchoPetCommandEvent<PlayerCommandSourceContainer> event, @Convert(PetConverters.Selected.class) Pet pet) {
        if (pet != null) {
            PetCommand.applyData(pet, event.getVariable("data"));
        }
        return true;
    }

    @Command(
            syntax = "name rider [name]",
            desc = "Sets the name of the rider of your currently selected pet",
            help = {"Use \"/pet view\" to select a pet to edit.", "If you only have one pet, there is no need to select one to edit.", "If a name is not provided in the command, you will be asked to enter a name separately", "Names can be more than one word if enclosed in single or double quotations e.g. sheep \"name:My cool pet\""}
    )
    @Authorize(Perm.NAME)
    public boolean riderName(EchoPetCommandEvent<PlayerCommandSourceContainer> event, @Convert(PetConverters.Selected.class) Pet pet) {
        if (pet == null) {
            return true;
        }

        if (pet.getRider() == null) {
            event.respond(Lang.NO_RIDER_FOUND.getValue("name", pet.getName()));
            return true;
        }

        String name = event.var("name");

        if (name == null) {
            // TODO: ask for name input
            //NameFactory.askForName(event.sender(), pet.getRider(), false);
        } else {
            pet.getRider().setName(name, true);
            event.respond(Lang.NAME_RIDER.getValue("name", pet.getName(), "newname", name));
        }
        return true;
    }

    @Command(
            syntax = "rider remove",
            desc = "Removes the rider of your currently selected pet",
            help = {"Use \"/pet view\" to select a pet to edit.", "If you only have one pet, there is no need to select one to edit.", "Removes the rider of a pet"}
    )
    @Authorize(Perm.REMOVE)
    public boolean removeRider(EchoPetCommandEvent<PlayerCommandSourceContainer> event, @Convert(PetConverters.Selected.class) Pet pet) {
        if (pet == null) {
            return true;
        }

        if (pet.getRider() == null) {
            event.respond(Lang.NO_RIDER_FOUND.getValue("name", pet.getName()));
            return true;
        }

        pet.removeRider();
        event.respond(Lang.RIDER_REMOVED.getValue("name", pet.getName()));
        return true;
    }
}