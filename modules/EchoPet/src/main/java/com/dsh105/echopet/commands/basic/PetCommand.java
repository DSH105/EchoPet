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

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.api.config.Lang;
import com.dsh105.echopet.api.entity.PetData;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.commands.PetConverters;
import com.dsh105.echopet.util.Perm;
import com.dsh105.influx.CommandListener;
import com.dsh105.influx.Controller;
import com.dsh105.influx.InfluxBukkitManager;
import com.dsh105.influx.annotation.*;
import com.dsh105.influx.dispatch.BukkitCommandEvent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.regex.Pattern;

@Nest(nests = {"pet"})
@Authorize(Perm.PET)
public class PetCommand implements CommandListener {

    public PetCommand() {
        InfluxBukkitManager manager = EchoPet.getCommandManager();
        manager.register(this);
        manager.nestCommandsIn(this, new HelpCommand());
        manager.nestCommandsIn(this, new RiderCommand());
        manager.nestCommandsIn(this, new NameCommand());
        manager.nestCommandsIn(this, new RemoveCommand());
        manager.nestCommandsIn(this, new InfoCommand());
        manager.nestCommandsIn(this, new ListCommand());
        manager.nestCommandsIn(this, new CallCommand());
        manager.nestCommandsIn(this, new HatCommand());
        manager.nestCommandsIn(this, new MenuCommand());
        manager.nestCommandsIn(this, new RideCommand());
        manager.nestCommandsIn(this, new SelectorCommand());
        manager.nestCommandsIn(this, new ToggleCommand());
        manager.nestCommandsIn(this, new SitCommand());
    }

    @Command(
            syntax = "",
            desc = "Manage your own pets",
            usage = "Use \"/pet help\" for help."
    )
    @Authorize(Perm.PET)
    public boolean parent(BukkitCommandEvent event) {
        event.respond(Lang.PLUGIN_INFORMATION.getValue("version", EchoPet.getCore().getDescription().getVersion()));
        return true;
    }

    @Command(
            syntax = "<type>",
            desc = "Creates a new pet of the given type",
            help = {"Set pet data using \"/pet [pet_name] data <data...\" (separated by spaces)", "Set the name of your pet using \"/pet [pet_name] name [name...]\""}
    )
    @Authorize(Perm.TYPE)
    @Nested
    public boolean create(BukkitCommandEvent<Player> event, @Bind("type") @Convert(PetConverters.CreateType.class) Pet pet) {
        if (pet != null) {
            EchoPet.getManager().save(pet);
            event.respond(Lang.PET_CREATED.getValue("type", pet.getType().humanName()));
        }
        return true;
    }

    @Command(
            syntax = "[pet_name] data <data...>",
            desc = "Applies the given data types to your pet (specified by [pet_name] or nothing if you only have one pet)",
            help = {"[pet_name] is the name of an existing pet e.g. \"My pet\" (in quotations)"}
    )
    @Authorize(Perm.DATA)
    @Nested
    public boolean applyData(BukkitCommandEvent<Player> event, @Bind("pet_name") @Default("") @Convert(PetConverters.FindPet.class) Pet pet) {
        if (pet != null) {
            ArrayList<String> invalidData = new ArrayList<>();
            ArrayList<PetData> validData = new ArrayList<>();
            ArrayList<String> validStringData = new ArrayList<>();
            for (String candidate : event.getVariable("data").getConsumedArguments()) {
                if (GeneralUtil.isEnumType(PetData.class, candidate)) {
                    invalidData.add(candidate);
                    continue;
                }
                validData.add(PetData.valueOf(candidate));
            }

            if (!invalidData.isEmpty()) {
                event.respond(Lang.INVALID_PET_DATA.getValue("data", StringUtil.combine("{c1}, {c2}", invalidData)));
            }

            if (!validData.isEmpty()) {
                pet.setDataValue(validData.toArray(new PetData[0]));
                event.respond(Lang.DATA_APPLIED.getValue("data", StringUtil.combine("{c1}, {c2}", validStringData), "name", pet.getName()));
            } else if (!invalidData.isEmpty()) {
                event.respond(Lang.NO_DATA_APPLIED.getValue("name", pet.getName()));
            }
        }
        return true;
    }
}