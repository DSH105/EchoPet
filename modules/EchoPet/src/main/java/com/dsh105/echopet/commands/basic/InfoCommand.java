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
import com.dsh105.echopet.api.entity.AttributeManager;
import com.dsh105.echopet.api.entity.PetData;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.inventory.ViewMenu;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.commands.PetConverters;
import com.dsh105.echopet.util.Perm;
import com.dsh105.influx.CommandListener;
import com.dsh105.influx.annotation.Authorize;
import com.dsh105.influx.annotation.Bind;
import com.dsh105.influx.annotation.Command;
import com.dsh105.influx.annotation.Convert;
import com.dsh105.influx.dispatch.BukkitCommandEvent;
import com.dsh105.powermessage.core.PowerMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class InfoCommand implements CommandListener {

    // TODO: merge this with the "view" command

    @Command(
            syntax = "info",
            desc = "Retrieve info on all of your active pets"
    )
    @Authorize(Perm.INFO)
    public boolean info(BukkitCommandEvent<Player> event) {
        List<Pet> pets = EchoPet.getManager().getPetsFor(event.sender());
        if (pets.size() <= 0) {
            event.respond(Lang.NO_PETS_FOUND.getValue());
            return true;
        }

        for (Pet pet : pets) {
            ViewMenu.displayInfo(pet);
        }
        event.respond(Lang.HOVER_TIP.getValue());
        return true;
    }

    @Command(
            syntax = "<pet_name> info",
            desc = "Retrieve info on one of your active pets (specified by <pet_name> or nothing if you only have one pet)",
            help = "<pet_name> is the name of an existing pet e.g. \"My pet\" (in quotations)"
    )
    @Authorize(Perm.INFO)
    public boolean infoForPet(BukkitCommandEvent<Player> event, @Bind("pet_name") @Convert(PetConverters.ByName.class) Pet pet) {
        if (pet == null) {
            return true;
        }

        ViewMenu.displayInfo(pet);
        event.respond(Lang.HOVER_TIP.getValue());
        return true;
    }
}