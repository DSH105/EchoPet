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

package com.dsh105.echopet.commands.basic;

import com.dsh105.command.Command;
import com.dsh105.command.CommandEvent;
import com.dsh105.command.CommandListener;
import com.dsh105.echopet.api.config.Lang;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.util.Perm;
import org.bukkit.entity.Player;

public class RemoveCommand implements CommandListener {

    @Command(
            command = "<pet_name> remove",
            description = "Removes an existing pet (specified by <pet_name>",
            permission = Perm.REMOVE,
            help = {"<pet_name> is the name of an existing pet", "Removes an existing pet"}
    )
    public boolean removePet(CommandEvent<Player> event) {
        Pet pet = PetCommand.getPetByName(event.sender(), event.variable("pet_name"));
        if (pet == null) {
            return true;
        }
        EchoPet.getManager().removePet(pet);
        event.respond(Lang.PET_REMOVED.getValue("name", pet.getName()));
        return true;
    }

    @Command(
            command = "remove",
            description = "Removes an existing pet",
            permission = Perm.REMOVE,
            help = {"Removes an existing pet"}
    )
    public boolean remove(CommandEvent<Player> event) {
        Pet pet = PetCommand.getSinglePet(event.sender(), "<pet_name> remove");
        if (pet == null) {
            return true;
        }
        EchoPet.getManager().removePet(pet);
        event.respond(Lang.PET_REMOVED.getValue("name", pet.getName()));
        return true;
    }
}