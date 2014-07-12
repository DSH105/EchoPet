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
import com.dsh105.command.ParentCommand;
import com.dsh105.commodus.GeneralUtil;
import com.dsh105.commodus.StringUtil;
import com.dsh105.echopetv3.api.config.Lang;
import com.dsh105.echopetv3.api.entity.PetData;
import com.dsh105.echopetv3.api.entity.PetType;
import com.dsh105.echopetv3.api.entity.pet.Pet;
import com.dsh105.echopetv3.api.plugin.EchoPet;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@Command(
        command = "pet",
        description = "Manage your own pets.",
        permission = "echopet.pet",
        usage = "Use \"/pet help\" for help."
)
public class PetCommand implements CommandListener {

    @ParentCommand
    public boolean parent(CommandEvent event) {
        event.respond(Lang.PLUGIN_INFORMATION.getValue("version", EchoPet.getCore().getDescription().getVersion()));
        return true;
    }

    public class Create {

        @Command(
                command = "<type> [name] [data]",
                description = "Creates a new pet of the given type",
                permission = "echopet.pet.type.<type>",
                help = {"Data values can be separated by a space", "e.g. blue,baby (for a sheep)", "If no pet name is provided, a default will be assigned", "Pet names can also be set using the \"name\" command"}
        )
        public boolean create(CommandEvent<Player> event) {
            if (!GeneralUtil.isEnumType(PetType.class, event.variable("type"))) {
                event.respond(Lang.INVALID_PET_TYPE.getValue("type", event.variable("type")));
                return true;
            }

            PetType petType = PetType.valueOf(event.variable("type").toUpperCase());
            String name = event.variable("name") == null ? petType.getDefaultName(event.sender().getName()) : event.variable("name");

            Pet pet = EchoPet.getManager().create(event.sender(), petType, true);
            if (pet == null) {
                return true;
            }
            pet.setName(name);

            if (event.variable("data") != null) {
                ArrayList<String> invalidData = new ArrayList<>();
                ArrayList<PetData> validData = new ArrayList<>();
                for (String data : event.variable("data").split(",")) {
                    if (!GeneralUtil.isEnumType(PetData.class, data)) {
                        invalidData.add(data);
                        continue;
                    }
                    validData.add(PetData.valueOf(data));
                }

                if (!validData.isEmpty()) {
                    pet.setDataValue(validData.toArray(new PetData[0]));
                }

                if (!invalidData.isEmpty()) {
                    event.respond(Lang.INVALID_PET_DATA.getValue("data", StringUtil.combine("{c1}, {c2}", invalidData)));
                }
            }

            EchoPet.getManager().save(pet);
            event.respond(Lang.PET_CREATED.getValue("type", petType.humanName()));
            return true;
        }
    }
}