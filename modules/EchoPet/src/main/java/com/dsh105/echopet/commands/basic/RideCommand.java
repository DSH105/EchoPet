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
import com.dsh105.echopet.util.Perm;
import com.dsh105.influx.CommandListener;
import com.dsh105.influx.annotation.*;
import com.dsh105.influx.dispatch.BukkitCommandEvent;
import org.bukkit.entity.Player;

public class RideCommand implements CommandListener {

    @Command(
            syntax = "ride",
            desc = "Ride your currently selected pet",
            help = {"Use \"/pet view\" to select a pet to edit.", "If you only have one pet, there is no need to select one to edit.", "Ride your pet", "Control your pet using the WASD keys and the space bar (to jump)", "Remember, some pets might be able to fly!"}
    )
    @Authorize(Perm.RIDE)
    public boolean ride(BukkitCommandEvent<Player> event, @Convert(PetConverters.Selected.class) Pet pet) {
        if (pet == null) {
            return true;
        }
        boolean status = pet.isOwnerRiding();
        pet.setOwnerRiding(!status);
        event.respond((status ? Lang.PET_RIDE_ON : Lang.PET_RIDE_OFF).getValue("name", pet.getName()));
        return true;
    }

}
