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

public class SitCommand implements CommandListener {

    @Command(
            syntax = "sit <state>",
            desc = "Sets the state of rest that your currently selected pet exhibits.",
            help = {"Use \"/pet view\" to select a pet to edit.", "If you only have one pet, there is no need to select one to edit.", "<state> refers to either yes or no, depending on whether you wish to set the pet sitting or not", "Pets that are sitting will remain stationary"}
    )
    @Authorize(Perm.SIT)
    public boolean sit(BukkitCommandEvent<Player> event, @Bind("state") boolean flag, @Convert(PetConverters.Selected.class) Pet pet) {
        if (pet == null) {
            return true;
        }

        pet.setStationary(flag);
        if (flag) {
            event.respond((pet.isStationary() ? Lang.PET_ALREADY_SITTING : Lang.PET_SITTING).getValue("name", pet.getName()));
        } else {
            event.respond((pet.isStationary() ? Lang.PET_NOT_SITTING : Lang.PET_ALREADY_NOT_SITTING).getValue("name", pet.getName()));
        }
        return true;
    }
}