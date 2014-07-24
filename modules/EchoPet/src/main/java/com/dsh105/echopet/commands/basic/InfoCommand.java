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

import com.dsh105.command.Command;
import com.dsh105.command.CommandEvent;
import com.dsh105.command.CommandListener;
import com.dsh105.commodus.GeneralUtil;
import com.dsh105.echopet.api.config.Lang;
import com.dsh105.echopet.api.entity.AttributeAccessor;
import com.dsh105.echopet.api.entity.PetData;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.util.Perm;
import com.dsh105.powermessage.core.PowerMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InfoCommand implements CommandListener {

    @Command(
            command = "info",
            description = "Retrieve info on all of your active pets",
            permission = Perm.INFO
    )
    public boolean info(CommandEvent<Player> event) {
        List<Pet> pets = EchoPet.getManager().getPetsFor(event.sender());
        if (pets.size() <= 0){
            event.respond(Lang.NO_PETS_FOUND.getValue());
            return true;
        }

        for (Pet pet : pets) {
            displayInfo(pet);
        }
        event.respond(Lang.HOVER_TIP.getValue());
        return true;
    }

    @Command(
            command = "<pet_name> info",
            description = "Retrieve info on one of your active pets (specified by <pet_name>)",
            permission = Perm.INFO,
            help = "<pet_name> is the name of an existing pet"
    )
    public boolean infoForPet(CommandEvent<Player> event) {
        List<Pet> pets = EchoPet.getManager().getPetsFor(event.sender());
        if (pets.size() <= 0){
            event.respond(Lang.NO_PETS_FOUND.getValue());
            return true;
        }
        Pet pet = PetCommand.getPetByName(event.sender(), event.variable("pet_name"));
        if (pet == null) {
            return true;
        }

        displayInfo(pet);
        event.respond(Lang.HOVER_TIP.getValue());
        return true;
    }

    private void displayInfo(Pet pet) {
        ChatColor format = EchoPet.getCommandManager().getFormatColour();
        ChatColor highlight = EchoPet.getCommandManager().getHighlightColour();

        PowerMessage message = new PowerMessage("• " + format + pet.getType().humanName() + " (" + highlight + pet.getName() + format + ")");

        StringBuilder dataBuilder = new StringBuilder();
        List<PetData> activeData = AttributeAccessor.getActiveDataValues(pet);
        if (!activeData.isEmpty()) {
            dataBuilder.append(format).append("Valid data types: ");
            for (PetData data : activeData) {
                if (dataBuilder.length() >= 35) {
                    dataBuilder.append("\n");
                }
                dataBuilder.append(highlight).append(data.humanName()).append(format).append(", ");
            }
            message.tooltip(dataBuilder.substring(0, dataBuilder.length() - 2));
        }
    }
}