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

package com.dsh105.echopet.api.commands.user;

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.api.commands.PetConverters;
import com.dsh105.echopet.api.commands.influx.EchoPetCommandEvent;
import com.dsh105.echopet.api.commands.influx.EchoPetCommandManager;
import com.dsh105.echopet.api.configuration.Lang;
import com.dsh105.echopet.api.entity.PetData;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.inventory.ViewMenu;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.bridge.container.PlayerCommandSourceContainer;
import com.dsh105.echopet.util.Perm;
import com.dsh105.influx.CommandListener;
import com.dsh105.influx.annotation.*;
import com.dsh105.influx.syntax.ContextualVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Nest(nests = {"pet"})
@Authorize(Perm.PET)
public class PetCommand implements CommandListener {

    public PetCommand() {
        EchoPetCommandManager manager = EchoPet.getCommandManager();
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

    // FIXME
    protected static void applyData(Pet pet, ContextualVariable variable) {
        List<String> invalidData = new ArrayList<>();
        List<PetData> validData = new ArrayList<>();
        List<String> validStringData = new ArrayList<>();
        for (String candidate : variable.getConsumedArguments()) {
            if (GeneralUtil.isEnumType(PetData.class, candidate)) {
                invalidData.add(candidate);
                continue;
            }
            validData.add(PetData.valueOf(candidate.toUpperCase()));
        }

        if (!invalidData.isEmpty()) {
            variable.getContext().respond(Lang.INVALID_PET_DATA.getValue("data", StringUtil.combine("{c1}, {c2}", invalidData)));
        }

        if (!validData.isEmpty()) {
            pet.setDataValue(validData.toArray(new PetData[0]));
            variable.getContext().respond(Lang.DATA_APPLIED.getValue("data", StringUtil.combine("{c1}, {c2}", validStringData), "name", pet.getName()));
        } else if (!invalidData.isEmpty()) {
            variable.getContext().respond(Lang.NO_DATA_APPLIED.getValue("name", pet.getName()));
        }
    }

    @Command(
            syntax = "",
            desc = "Manage your own pets",
            usage = "Use \"/pet help\" for help."
    )
    @Authorize(Perm.PET)
    public boolean parent(EchoPetCommandEvent event) {
        // TODO: only show this in /petadmin
        event.respond(Lang.PLUGIN_INFORMATION.getValue("version", EchoPet.getNucleus().getPluginVersion()));
        return true;
    }

    @Command(
            syntax = "view",
            desc = "Apply changes to any of your existing pets.",
            help = {"Select a pet to edit (shows information on all your pets)."}
    )
    @Authorize(Perm.VIEW)
    @Nested
    public boolean view(EchoPetCommandEvent<PlayerCommandSourceContainer> event) {
        List<Pet> pets = EchoPet.getManager().getPetsFor(event.sender().get().getUID());
        if (pets.isEmpty()) {
            event.respond(Lang.NO_PETS_FOUND.getValue());
            return true;
        }
        ViewMenu.getInventory(pets).show(event.sender().get());
        return true;
    }

    @Command(
            syntax = "uuid <pet_uuid> [pet_name]",
            desc = "Select a pet by its ID.",
            help = "Not intended for external use."
    )
    @Authorize(Perm.UUID_SELECT)
    @Nested
    @Hidden
    // Internal use by "/pet view" - that's why it's hidden ;D
    public boolean selectByUniqueId(EchoPetCommandEvent<PlayerCommandSourceContainer> event, @Verify("^(?!none$).*") @Bind("pet_uuid") UUID petUniqueId, @Bind("pet_name") String name) {
        PetConverters.selectPet(event.sender().get().getUID(), petUniqueId);
        event.respond((name == null ? Lang.PET_SELECTED : Lang.PET_SELECTED_OF_NAME).getValue("name", name));
        return true;
    }

    @Command(
            syntax = "uuid <pet_uuid> none",
            desc = "Remove any pet selection.",
            help = "Not intended for external use."
    )
    @Authorize(Perm.UUID_SELECT)
    @Nested
    @Hidden
    // Internal use by "/pet view" - that's why it's hidden ;D
    public boolean selectNone(EchoPetCommandEvent<PlayerCommandSourceContainer> event) {
        PetConverters.selectPet(event.sender().get().getUID(), null);
        // TODO: send a message perhaps?
        return true;
    }

    @Command(
            syntax = "create <type>",
            desc = "Creates a new pet of the given type",
            help = {"Set pet data using \"/pet [pet_name] data <data...\" (separated by spaces)", "Set the name of your pet using \"/pet [pet_name] name [name...]\""}
    )
    @Authorize(Perm.TYPE)
    @Nested
    public boolean create(EchoPetCommandEvent<PlayerCommandSourceContainer> event, @Bind("type") @Convert(PetConverters.CreateType.class) Pet pet) {
        if (pet != null) {
            EchoPet.getManager().save(pet);
            event.respond(Lang.PET_CREATED.getValue("type", pet.getType().humanName()));
        }
        return true;
    }

    @Command(
            syntax = "data <data...>",
            desc = "Applies the given data types to your currently selected pet.",
            help = {"Use \"/pet view\" to select a pet to edit.", "If you only have one pet, there is no need to select one to edit."}
    )
    @Authorize(Perm.DATA)
    @Nested
    public boolean applyData(EchoPetCommandEvent<PlayerCommandSourceContainer> event, @Convert(PetConverters.Selected.class) Pet pet) {
        if (pet != null) {
            applyData(pet, event.getVariable("data"));
        }
        return true;
    }
}