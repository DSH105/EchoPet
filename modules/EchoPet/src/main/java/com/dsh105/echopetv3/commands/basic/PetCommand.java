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

import com.dsh105.command.*;
import com.dsh105.commodus.GeneralUtil;
import com.dsh105.commodus.StringUtil;
import com.dsh105.echopetv3.api.config.Lang;
import com.dsh105.echopetv3.api.entity.PetData;
import com.dsh105.echopetv3.api.entity.PetType;
import com.dsh105.echopetv3.api.entity.pet.Pet;
import com.dsh105.echopetv3.api.plugin.EchoPet;
import com.dsh105.echopetv3.commands.admin.ReloadCommand;
import com.dsh105.echopetv3.util.Perm;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Command(
        command = "pet",
        description = "Manage your own pets",
        permission = Perm.PET,
        usage = "Use \"/pet help\" for help."
)
public class PetCommand implements CommandListener {

    protected static final String PET_REGEX_STRING = "([^\\s]+)(:[^\\s]+)?(;.+)?";
    protected static final String PET_VARIABLE_NAME = "<type>:[data];[name...]";
    protected static final Pattern PET_REGEX_PATTERN = Pattern.compile(PET_REGEX_STRING);

    public PetCommand() {
        CommandManager manager = EchoPet.getCommandManager();
        manager.register(this);
        manager.registerSubCommands(this, new Create());
        manager.registerSubCommands(this, new HelpCommand());
        manager.registerSubCommands(this, new RiderCommand());
        manager.registerSubCommands(this, new NameCommand());
        manager.registerSubCommands(this, new RemoveCommand());
        manager.registerSubCommands(this, new InfoCommand());
        manager.registerSubCommands(this, new ListCommand());
        manager.registerSubCommands(this, new CallCommand());
        manager.registerSubCommands(this, new HatCommand());
        manager.registerSubCommands(this, new MenuCommand());
        manager.registerSubCommands(this, new RideCommand());
        manager.registerSubCommands(this, new SelectCommand());
        manager.registerSubCommands(this, new SelectorCommand());
        //manager.registerSubCommands(this, new ToggleCommand());
        manager.registerSubCommands(this, new SitCommand());
    }

    @ParentCommand
    public boolean parent(CommandEvent event) {
        event.respond(Lang.PLUGIN_INFORMATION.getValue("version", EchoPet.getCore().getDescription().getVersion()));
        return true;
    }

    public class Create implements CommandListener {
        @Command(
                command = "<r:" + PET_REGEX_STRING + ",n:" + PET_VARIABLE_NAME + ">",
                description = "Creates a new pet of the given type",
                permission = Perm.TYPE,
                help = {"Data values can be separated by a space", "e.g. blue,baby (for a sheep)", "Names can be more than one word", "If no pet name is provided, a default will be assigned", "Pet names can also be set using the \"name\" command"}
        )
        public boolean create(CommandEvent<Player> event) {
            String[] inputParts = petFromVariables(event.variable("<type>:[data];[name...]"));
            String petType = inputParts[0];
            String data = inputParts[1];
            String name = inputParts[2];

            if (!GeneralUtil.isEnumType(PetType.class, petType)) {
                event.respond(Lang.INVALID_PET_TYPE.getValue("type", petType));
                return true;
            }

            PetTemp temp = PetTemp.build(petType, name, data, event.sender());

            Pet pet = EchoPet.getManager().create(event.sender(), temp.getPetType(), true);
            if (pet == null) {
                return true;
            }
            pet.setName(temp.getName());

            if (!temp.getValidPetData().isEmpty()) {
                pet.setDataValue(temp.getValidPetData().toArray(new PetData[0]));
            }
            if (!temp.getInvalidPetData().isEmpty()) {
                event.respond(Lang.INVALID_PET_DATA.getValue("data", StringUtil.combine("{c1}, {c2}", temp.getInvalidPetData())));
            }

            EchoPet.getManager().save(pet);
            event.respond(Lang.PET_CREATED.getValue("type", temp.getPetType().humanName()));
            return true;
        }
    }

    protected static String[] petFromVariables(String input) {
        String[] parts = input.split(":");
        String petType = parts[0];
        String[] dataParts = parts.length == 1 ? new String[]{"", ""} : parts[1].split(";");
        String data = dataParts[0];
        String name = StringUtil.combineArray(1, " ", dataParts);
        return new String[] {petType, data, name};
    }

    protected static Pet getPetByName(Player owner, String petName) {
        Pet pet = EchoPet.getManager().getPetByName(owner, petName);
        if (pet == null) {
            Lang.PET_NOT_FOUND.send(owner, "name", petName);
        }
        return pet;
    }

    protected static Pet getSinglePet(Player owner, String command) {
        List<Pet> pets = EchoPet.getManager().getPetsFor(owner);
        if (pets.size() <= 0){
            Lang.NO_PETS_FOUND.send(owner);
            return null;
        } else if (pets.size() > 1) {
            Lang.MORE_PETS_FOUND.send(owner, "command", command);
            return null;
        }

        return pets.get(0);
    }

    protected static class PetTemp {

        private PetType petType;
        private String name;
        private ArrayList<PetData> validPetData;
        private ArrayList<String> invalidPetData;

        public PetTemp(PetType petType, String name, ArrayList<PetData> validPetData, ArrayList<String> invalidPetData) {
            this.petType = petType;
            this.name = name;
            this.validPetData = validPetData;
            this.invalidPetData = invalidPetData;
        }

        public PetTemp(PetType petType, String name) {
            this(petType, name, new ArrayList<PetData>(), new ArrayList<String>());
        }

        public PetType getPetType() {
            return petType;
        }

        public String getName() {
            return name;
        }

        public ArrayList<PetData> getValidPetData() {
            return validPetData;
        }

        public ArrayList<String> getInvalidPetData() {
            return invalidPetData;
        }

        public static PetTemp build(String petTypeVar, String nameVar, String dataVar, Player owner) {
            PetType petType = PetType.valueOf(petTypeVar.toUpperCase());
            String name = nameVar == null || nameVar.isEmpty() ? petType.getDefaultName(owner.getName()) : nameVar;

            if (dataVar == null || dataVar.isEmpty()) {
                return new PetTemp(petType, name);
            }

            ArrayList<String> invalidData = new ArrayList<>();
            ArrayList<PetData> validData = new ArrayList<>();
            for (String data : dataVar.split(",")) {
                if (!GeneralUtil.isEnumType(PetData.class, data)) {
                    invalidData.add(data);
                    continue;
                }
                validData.add(PetData.valueOf(data));
            }

            return new PetTemp(petType, name, validData, invalidData);
        }
    }
}