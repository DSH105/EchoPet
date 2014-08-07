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

public class RideCommand implements CommandListener {

    @Command(
            command = "<pet_name> ride",
            description = "Ride your pet (specified by <pet_name>)",
            permission = Perm.RIDE,
            help = {"<pet_name> is the name of an existing pet", "Ride your pet", "Control your pet using the WASD keys and the space bar (to jump)", "Remember, some pets might be able to fly!"}
    )
    public boolean ridePet(CommandEvent<Player> event) {
        Pet pet = PetCommand.getPetByName(event.sender(), event.variable("pet_name"));
        if (pet == null) {
            return true;
        }
        boolean status = pet.isOwnerRiding();
        pet.setOwnerRiding(!status);
        event.respond((status ? Lang.PET_RIDE_ON : Lang.PET_RIDE_OFF).getValue("name", pet.getName()));
        return true;
    }

    @Command(
            command = "ride",
            description = "Ride your pet",
            permission = Perm.RIDE,
            help = {"Ride your pet", "Control your pet using the WASD keys and the space bar (to jump)", "Remember, some pets might be able to fly!"}
    )
    public boolean ride(CommandEvent<Player> event) {
        Pet pet = PetCommand.getSinglePet(event.sender(), "<pet_name> ride");
        if (pet == null) {
            return true;
        }
        boolean status = pet.isOwnerRiding();
        pet.setOwnerRiding(!status);
        event.respond((status ? Lang.PET_RIDE_ON : Lang.PET_RIDE_OFF).getValue("name", pet.getName()));
        return true;
    }
}
