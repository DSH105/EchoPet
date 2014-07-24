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
import com.dsh105.echopet.util.Perm;
import org.bukkit.entity.Player;

public class SitCommand implements CommandListener {

    @Command(
            command = "<r:(?i)yes|true|no|false,n:state>",
            description = "Sets the state of rest that a pet exhibits",
            permission = Perm.SIT,
            help = {"<state> refers to either yes or no, depending on whether you wish to set the pet sitting or not", "Pets that are sitting will remain stationary"}
    )
    public boolean sit(CommandEvent<Player> event) {
        Pet pet = PetCommand.getSinglePet(event.sender(), "<pet_name> sit");
        if (pet == null) {
            return true;
        }

        if (event.variable("state").equalsIgnoreCase("yes") || event.variable("state").equalsIgnoreCase("true")) {
            if (pet.isStationary()) {
                event.respond(Lang.PET_ALREADY_SITTING.getValue("name", pet.getName()));
                return true;
            }
            pet.setStationary(true);
            event.respond(Lang.PET_SITTING.getValue("name", pet.getName()));
        } else {
            if (!pet.isStationary()) {
                event.respond(Lang.PET_ALREADY_NOT_SITTING.getValue("name", pet.getName()));
                return true;
            }
            pet.setStationary(false);
            event.respond(Lang.PET_NOT_SITTING.getValue("name", pet.getName()));
        }
        return true;
    }

    @Command(
            command = "<pet_name> <r:(?i)yes|true|no|false,n:state>",
            description = "Sets the state of rest that a pet exhibits",
            permission = Perm.SIT,
            help = {"<pet_name> is the name of an existing pet", "<state> refers to either yes or no, depending on whether you wish to set the pet sitting or not", "Pets that are sitting will remain stationary"}
    )
    public boolean sitPet(CommandEvent<Player> event) {
        Pet pet = PetCommand.getPetByName(event.sender(), event.variable("pet_name"));
        if (pet == null) {
            return true;
        }

        if (event.variable("state").equalsIgnoreCase("yes") || event.variable("state").equalsIgnoreCase("true")) {
            if (pet.isStationary()) {
                event.respond(Lang.PET_ALREADY_SITTING.getValue("name", pet.getName()));
                return true;
            }
            pet.setStationary(true);
            event.respond(Lang.PET_SITTING.getValue("name", event.variable("pet_name")));
        } else {
            if (!pet.isStationary()) {
                event.respond(Lang.PET_ALREADY_NOT_SITTING.getValue("name", event.variable("pet_name")));
                return true;
            }
            pet.setStationary(false);
            event.respond(Lang.PET_NOT_SITTING.getValue("name", event.variable("pet_name")));
        }
        return true;
    }
}