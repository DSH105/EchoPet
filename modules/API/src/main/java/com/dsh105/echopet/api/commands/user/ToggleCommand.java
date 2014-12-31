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

import com.dsh105.echopet.api.commands.PetConverters;
import com.dsh105.echopet.api.commands.influx.EchoPetCommandEvent;
import com.dsh105.echopet.api.configuration.Lang;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.bridge.Ident;
import com.dsh105.echopet.bridge.container.PlayerCommandSourceContainer;
import com.dsh105.echopet.util.Perm;
import com.dsh105.influx.CommandListener;
import com.dsh105.influx.annotation.Authorize;
import com.dsh105.influx.annotation.Command;
import com.dsh105.influx.annotation.Convert;

public class ToggleCommand implements CommandListener {

    /*@Command(
            syntax = "toggle",
            desc = "Toggles an existing pet"
    )
    @Authorize(Perm.TOGGLE)
    public boolean toggle(BukkitCommandEvent<Player> event) {
        List<Pet> pets = EchoPet.getManager().getPetsFor(event.sender());
        // Is there a pet to hide?
        if (pets.size() > 0) {
            if (pets.size() == 1) {
                Pet pet = pets.get(0);
                EchoPet.getManager().save(pet);
                EchoPet.getManager().removePet(pet);
                event.respond(Lang.PET_HIDDEN.getValue("name", pet.getName()));
                return true;
            } else {
                event.respond(Lang.MORE_PETS_FOUND.getValue("command", "<pet_name> toggle"));
                return true;
            }
        }

        // Nope, let's look for a pet to load

        ConfigurationSection section = EchoPet.getConfig(ConfigType.DATA).getConfigurationSection(Data.SECTION.getValue(IdentUtil.getIdentificationForAsString(event.sender())));
        if (section == null) {
            event.respond(Lang.NO_PETS_FOUND.getValue());
            return true;
        }

        Map<String, Object> values = section.getValues(false);
        if (values.isEmpty()) {
            event.respond(Lang.NO_PETS_FOUND.getValue());
            return true;
        }
        if (values.size() != 1) {
            event.respond(Lang.MORE_PETS_FOUND.getValue("command", "<pet_name> toggle"));
            return true;
        }

        Pet pet = EchoPet.getManager().loadPet(event.sender(), values.keySet().toArray(StringUtil.EMPTY_STRING_ARRAY)[0]);
        if (pet == null) {
            event.respond(Lang.PET_NOT_LOADED_UNEXPECTED.getValue());
            return true;
        }
        event.respond(Lang.PET_SHOWN.getValue("name", pet.getName()));
        return true;
    }*/

    @Command(
            syntax = "toggle",
            desc = "Toggles your currently selected pet.",
            help = {"Use \"/pet view\" to select a pet to edit.", "If you only have one pet, there is no need to select one to edit."}
    )
    @Authorize(Perm.TOGGLE)
    public boolean togglePet(EchoPetCommandEvent<PlayerCommandSourceContainer> event, @Convert(PetConverters.Selected.class) Pet pet) {
        // Does it exist?
        if (pet != null) {
            EchoPet.getManager().save(pet);
            EchoPet.getManager().removePet(pet);
            event.respond(Lang.PET_HIDDEN.getValue("name", pet.getName()));
            return true;
        }

        // TODO: lol..
        // TODO: open up a selection menu?
        pet = EchoPet.getManager().load(event.sender().get().getUID(), EchoPet.getManager().getPetNameMapFor(Ident.get().getUID(event.sender())).get(event.var("pet_name")).toString());
        if (pet == null) {
            // Error shown through above messages
            return true;
        }

        event.respond(Lang.PET_SHOWN.getValue("name", pet.getName()));
        return true;
    }
}