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
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.commands.PetConverters;
import com.dsh105.echopet.util.Perm;
import com.dsh105.influx.CommandListener;
import com.dsh105.influx.InfluxBukkitManager;
import com.dsh105.influx.annotation.*;
import com.dsh105.influx.dispatch.BukkitCommandEvent;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class PetCommand implements CommandListener {

    protected static final String PET_REGEX_STRING = "([^\\s]+)(:[^\\s]+)?(;.+)?";
    protected static final String PET_VARIABLE_NAME = "<type>:[data];[name...]";
    protected static final Pattern PET_REGEX_PATTERN = Pattern.compile(PET_REGEX_STRING);

    public PetCommand() {
        InfluxBukkitManager manager = EchoPet.getCommandManager();
        manager.register(this);
        manager.nestCommandsIn(this, new HelpCommand(), false);
        manager.nestCommandsIn(this, new RiderCommand(), false);
        manager.nestCommandsIn(this, new NameCommand(), false);
        manager.nestCommandsIn(this, new RemoveCommand(), false);
        manager.nestCommandsIn(this, new InfoCommand(), false);
        manager.nestCommandsIn(this, new ListCommand(), false);
        manager.nestCommandsIn(this, new CallCommand(), false);
        manager.nestCommandsIn(this, new HatCommand(), false);
        manager.nestCommandsIn(this, new MenuCommand(), false);
        manager.nestCommandsIn(this, new RideCommand(), false);
        manager.nestCommandsIn(this, new SelectorCommand(), false);
        manager.nestCommandsIn(this, new ToggleCommand(), false);
        manager.nestCommandsIn(this, new SitCommand(), false);
    }

    @Command(
            syntax = "pet",
            desc = "Manage your own pets",
            usage = "Use \"/pet help\" for help."
    )
    @Authorize(Perm.PET)
    public boolean parent(BukkitCommandEvent event) {
        event.respond(Lang.PLUGIN_INFORMATION.getValue("version", EchoPet.getCore().getDescription().getVersion()));
        return true;
    }

    @Command(
            syntax = "<pet>",
            desc = "Creates a new pet of the given type",
            help = {"Data values can be separated by a comma", "e.g. blue,baby (for a sheep)", "Names can be more than one word if enclosed in single or double quotations e.g. sheep \"name:My cool pet\"", "If no pet name is provided, a default will be assigned", "Pet names can also be set using the \"name\" command"}
    )
    @Authorize(Perm.TYPE)
    public boolean create(BukkitCommandEvent<Player> event,
                          @Bind("pet") @Accept(value = 3, showAs = "<type> name:[name] data:[data]")
                          @Convert(PetConverters.Create.class) Pet pet) {
        if (pet == null) {
            return true;
        }
        EchoPet.getManager().save(pet);
        event.respond(Lang.PET_CREATED.getValue("type", pet.getType().humanName()));
        return true;
    }
}