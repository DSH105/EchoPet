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
import com.dsh105.echopetv3.util.Perm;
import org.bukkit.entity.Player;

public class HatCommand implements CommandListener {

    @Command(
            command = "<pet_name> hat",
            description = "Places your pet on your head",
            permission = Perm.HAT,
            help = {"<pet_name> is the name of an existing pet", "Have your pet ride on top of you", "Pets will appear floating higher on your screen than others to avoid screen blocking"}
    )
    public boolean hatPet(CommandEvent<Player> event) {
        Pet pet = PetCommand.getPetByName(event.sender(), event.variable("pet_name"));
        if (pet == null) {
            return true;
        }
        boolean status = pet.isHat();
        pet.setHat(!status);
        event.respond((status ? Lang.PET_HAT_ON : Lang.PET_HAT_OFF).getValue("name", event.variable("pet_name")));
        return true;
    }

    @Command(
            command = "hat",
            description = "Places your pet on your head",
            permission = Perm.HAT,
            help = {"Have your pet ride on top of you", "Pets will appear floating higher on your screen than others to avoid screen blocking"}
    )
    public boolean hat(CommandEvent<Player> event) {
        Pet pet = PetCommand.getSinglePet(event.sender(), "<pet_name> hat");
        if (pet == null) {
            return true;
        }
        boolean status = pet.isHat();
        pet.setHat(!status);
        event.respond((status ? Lang.PET_HAT_ON : Lang.PET_HAT_OFF).getValue("name", pet.getName()));
        return true;
    }
}