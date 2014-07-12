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
import com.dsh105.commodus.GeneralUtil;
import com.dsh105.commodus.StringUtil;
import com.dsh105.echopetv3.api.config.Lang;
import com.dsh105.echopetv3.api.entity.PetData;
import com.dsh105.echopetv3.api.entity.PetType;
import com.dsh105.echopetv3.api.entity.pet.Pet;
import com.dsh105.echopetv3.api.plugin.EchoPet;
import com.dsh105.echopetv3.commands.basic.PetCommand;
import com.dsh105.echopetv3.conversation.NameFactory;
import com.dsh105.echopetv3.util.Perm;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class RiderCommand implements CommandListener {

    @Command(
            command = "rider <type> [data] [name...]",
            description = "Creates a rider for an existing pet",
            permission = Perm.TYPE,
            help = {"Data values can be separated by a space", "e.g. blue,baby (for a sheep)", "If no pet name is provided, a default will be assigned", "Pet names can also be set using the \"rider name\" command"}
    )
    public boolean rider(CommandEvent<Player> event) {
        if (!GeneralUtil.isEnumType(PetType.class, event.variable("type"))) {
            event.respond(Lang.INVALID_PET_TYPE.getValue("type", event.variable("type")));
            return true;
        }

        Pet pet = PetCommand.getSinglePet(event.sender(), "<pet_name> rider <type> [data] [name...]");
        return pet == null || createRider(pet, PetCommand.PetTemp.build(event.variable("type"), event.variable("name"), event.variable("data"), event.sender()), event);
    }

    @Command(
            command = "<pet_name> rider <type> [data] [name...]",
            description = "Creates a rider for an existing pet (specified by <pet_name>)",
            permission = Perm.TYPE,
            help = {"<pet_name> is the name of an existing pet", "Data values can be separated by a space", "e.g. blue,baby (for a sheep)", "If no pet name is provided, a default will be assigned", "Pet names can also be set using the \"rider name\" command"}
    )
    public boolean riderForPet(CommandEvent<Player> event) {
        if (!GeneralUtil.isEnumType(PetType.class, event.variable("type"))) {
            event.respond(Lang.INVALID_PET_TYPE.getValue("type", event.variable("type")));
            return true;
        }

        Pet pet = PetCommand.getPetByName(event.sender(), event.variable("pet_name"));
        return pet == null || createRider(pet, PetCommand.PetTemp.build(event.variable("type"), event.variable("name"), event.variable("data"), event.sender()), event);
    }

    @Command(
            command = "name rider [name...]",
            description = "Sets the name of the rider of an existing pet",
            permission = Perm.NAME,
            help = {"If a name is not provided in the command, you will be asked to enter a name separately", "Names can be more than one word"}
    )
    public boolean riderName(CommandEvent<Player> event) {
        Pet pet = PetCommand.getSinglePet(event.sender(), "<pet_name> name rider [name...]");
        if (pet == null) {
            return true;
        }

        if (pet.getRider() == null) {
            event.respond(Lang.NO_RIDER_FOUND.getValue("name", pet.getName()));
            return true;
        }

        if (event.variable("name") == null) {
            NameFactory.askForName(event.sender(), pet.getRider(), false);
        } else {
            pet.getRider().setName(event.variable("name"), true);
        }
        return true;
    }

    @Command(
            command = "<pet_name> name rider [name...]",
            description = "Sets the name of the rider of an existing pet (specified by <pet_name>)",
            permission = Perm.NAME,
            help = {"<pet_name> is the name of an existing pet", "If a name is not provided in the command, you will be asked to enter a name separately", "Names can be more than one word"}
    )
    public boolean riderNameForPet(CommandEvent<Player> event) {
        Pet pet = PetCommand.getPetByName(event.sender(), event.variable("pet_name"));
        if (pet == null) {
            return true;
        }

        if (pet.getRider() == null) {
            event.respond(Lang.NO_RIDER_FOUND.getValue("name", event.variable("pet_name")));
            return true;
        }

        if (event.variable("name") == null) {
            NameFactory.askForName(event.sender(), pet.getRider(), false);
        } else {
            pet.getRider().setName(event.variable("name"), true);
        }
        return true;
    }

    @Command(
            command = "rider remove",
            description = "Removes the rider of an existing pet",
            permission = Perm.REMOVE,
            help = {"Removes the rider of a pet"}
    )
    public boolean removeRider(CommandEvent<Player> event) {
        Pet pet = PetCommand.getSinglePet(event.sender(), "<pet_name> rider remove");
        if (pet == null) {
            return true;
        }

        if (pet.getRider() == null) {
            event.respond(Lang.NO_RIDER_FOUND.getValue("name", pet.getName()));
            return true;
        }

        pet.despawnRider();
        event.respond(Lang.RIDER_REMOVED.getValue("name", pet.getName()));
        return true;
    }

    @Command(
            command = "<pet_name> rider remove",
            description = "Removes the rider of a pet (specified by <pet_name>)",
            permission = Perm.REMOVE,
            help = {"<pet_name> is the name of an existing pet", "Removes the rider of a pet"}
    )
    public boolean removeRiderForPet(CommandEvent<Player> event) {
        Pet pet = PetCommand.getPetByName(event.sender(), event.variable("pet_name"));
        if (pet == null) {
            return true;
        }

        if (pet.getRider() == null) {
            event.respond(Lang.NO_RIDER_FOUND.getValue("name", event.variable("pet_name")));
            return true;
        }

        pet.despawnRider();
        event.respond(Lang.RIDER_REMOVED.getValue("name", event.variable("pet_name")));
        return true;
    }

    private boolean createRider(Pet pet, PetCommand.PetTemp temp, CommandEvent<Player> event) {
        Pet rider = EchoPet.getManager().create(event.sender(), temp.getPetType(), true);
        if (pet == null) {
            return true;
        }
        rider.setName(temp.getName());

        if (!temp.getValidPetData().isEmpty()) {
            rider.setDataValue(temp.getValidPetData().toArray(new PetData[0]));
        }
        if (!temp.getInvalidPetData().isEmpty()) {
            event.respond(Lang.INVALID_PET_DATA.getValue("data", StringUtil.combine("{c1}, {c2}", temp.getInvalidPetData())));
        }

        EchoPet.getManager().save(pet);
        event.respond(Lang.RIDER_CREATED.getValue("type", temp.getPetType().humanName(), "name", pet.getName()));
        return true;
    }
}