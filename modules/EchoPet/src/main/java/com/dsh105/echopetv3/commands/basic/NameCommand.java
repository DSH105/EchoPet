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

package com.dsh105.echopetv3.commands.basic;

import com.dsh105.command.Command;
import com.dsh105.command.CommandEvent;
import com.dsh105.command.CommandListener;
import com.dsh105.echopetv3.api.config.Lang;
import com.dsh105.echopetv3.api.entity.pet.Pet;
import com.dsh105.echopetv3.api.plugin.EchoPet;
import com.dsh105.echopetv3.conversation.NameFactory;
import com.dsh105.echopetv3.util.Perm;
import org.bukkit.entity.Player;

import java.util.List;

public class NameCommand implements CommandListener {

    @Command(
            command = "<pet_name> name [name...]",
            description = "Sets the name of your pet",
            permission = Perm.NAME,
            help = {"<pet_name> is the name of an existing pet", "If a name is not provided in the command, you will be asked to enter a name separately", "Names can be more than one word"}
    )
    public boolean namePet(CommandEvent<Player> event) {
        Pet pet = PetCommand.getPetByName(event.sender(), event.variable("pet_name"));
        if (pet == null) {
            return true;
        }

        if (event.variable("name") == null) {
            NameFactory.askForName(event.sender(), pet, false);
        } else {
            pet.setName(event.variable("name"), true);
        }
        event.respond(Lang.NAME_PET.getValue("name", event.variable("pet_name"), "newname", event.variable("name")));
        return true;
    }

    @Command(
            command = "name [name...]",
            description = "Sets the name of your pet",
            permission = Perm.NAME,
            help = {"If a name is not provided in the command, you will be asked to enter a name separately", "Names can be more than one word"}
    )
    public boolean name(CommandEvent<Player> event) {
        Pet pet = PetCommand.getSinglePet(event.sender(), "<pet_name> name [name...]");
        if (pet == null) {
            return true;
        }

        if (event.variable("name") == null) {
            NameFactory.askForName(event.sender(), pet, false);
        } else {
            pet.setName(event.variable("name"), true);
        }
        event.respond(Lang.NAME_PET.getValue("name", pet.getName(), "newname", event.variable("name")));
        return true;
    }
}