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
import com.dsh105.echopetv3.conversation.NameFactory;
import com.dsh105.echopetv3.util.Perm;
import org.bukkit.entity.Player;

public class MenuCommand implements CommandListener {

    @Command(
            command = "<pet_name> menu",
            description = "Opens the pet data menu for a pet",
            permission = Perm.MENU,
            help = {"<pet_name> is the name of an existing pet"}
    )
    public boolean namePet(CommandEvent<Player> event) {
        Pet pet = PetCommand.getPetByName(event.sender(), event.variable("pet_name"));
        if (pet == null) {
            return true;
        }
        pet.onInteract(event.sender());
        return true;
    }

    @Command(
            command = "menu",
            description = "Opens the pet data menu for a pet",
            permission = Perm.MENU
    )
    public boolean name(CommandEvent<Player> event) {
        Pet pet = PetCommand.getSinglePet(event.sender(), "<pet_name> menu");
        if (pet == null) {
            return true;
        }
        pet.onInteract(event.sender());
        return true;
    }
}