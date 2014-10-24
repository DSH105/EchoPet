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

import com.dsh105.echopet.api.config.Lang;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.commands.PetConverters;
import com.dsh105.echopet.conversation.NameFactory;
import com.dsh105.echopet.util.Perm;
import com.dsh105.influx.CommandListener;
import com.dsh105.influx.annotation.*;
import com.dsh105.influx.dispatch.BukkitCommandEvent;
import org.bukkit.entity.Player;

public class RiderCommand implements CommandListener {

    @Command(
            syntax = "[pet_name] rider <pet>",
            desc = "Creates a rider for an existing pet (specified by [pet_name] or nothing if you only have one pet)",
            help = {"[pet_name] is the name of an existing pet e.g. \"My pet\" (in quotations)", "Data values can be separated by a comma", "e.g. blue,baby (for a sheep)", "Names can be more than one word if enclosed in single or double quotations e.g. sheep \"name:My cool pet\"", "If no pet name is provided, a default will be assigned", "Pet names can also be set using the \"rider name\" command"}
    )
    @Authorize(Perm.RIDER_TYPE)
    public boolean rider(BukkitCommandEvent<Player> event,
                               @Bind("pet") @Accept(value = 3, showAs = "<type> name:[name] data:[data]") @Convert(PetConverters.Create.class) Pet rider,
                               @Bind("pet_name") @Default("") @Convert(PetConverters.FindPet.class) Pet pet) {
        if (pet != null && rider != null) {
            pet.spawnRider(rider, true);
        }
        return true;
    }

    @Command(
            syntax = "[pet_name] name rider [name]",
            desc = "Sets the name of the rider of an existing pet (specified by [pet_name] or nothing if you only have one pet)",
            help = {"[pet_name] is the name of an existing pet e.g. \"My pet\" (in quotations)", "If a name is not provided in the command, you will be asked to enter a name separately", "Names can be more than one word if enclosed in single or double quotations e.g. sheep \"name:My cool pet\""}
    )
    @Authorize(Perm.NAME)
    public boolean riderName(BukkitCommandEvent<Player> event, @Bind("pet_name") @Default("") @Convert(PetConverters.FindPet.class) Pet pet) {
        if (pet == null) {
            return true;
        }

        if (pet.getRider() == null) {
            event.respond(Lang.NO_RIDER_FOUND.getValue("name", pet.getName()));
            return true;
        }

        String name = event.var("name");

        if (name == null) {
            NameFactory.askForName(event.sender(), pet.getRider(), false);
        } else {
            pet.getRider().setName(name, true);
        }
        return true;
    }

    @Command(
            syntax = "[pet_name] rider remove",
            desc = "Removes the rider of a pet (specified by [pet_name] or nothing if you only have one pet)",
            help = {"[pet_name] is the name of an existing pet e.g. \"My pet\" (in quotations)", "Removes the rider of a pet"}
    )
    @Authorize(Perm.REMOVE)
    public boolean removeRider(BukkitCommandEvent<Player> event, @Bind("pet_name") @Default("") @Convert(PetConverters.FindPet.class) Pet pet) {
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
}