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

package com.dsh105.echopet.api.entity.pet.type;

import com.dsh105.echopet.api.entity.PetInfo;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.SizeCategory;
import com.dsh105.echopet.api.entity.entitypet.type.EntityWitchPet;
import com.dsh105.echopet.api.entity.pet.EchoRangedPet;
import com.dsh105.echopet.api.entity.pet.PetBase;
import org.bukkit.entity.Player;
import org.bukkit.entity.Witch;

@PetInfo(type = PetType.WITCH, width = 0.6F, height = 1.9F)
public class EchoWitchPet extends EchoRangedPet<Witch, EntityWitchPet> implements WitchPet {

    public EchoWitchPet(Player owner) {
        super(owner);
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.REGULAR;
    }

    @Override
    public String getIdleSound() {
        return "mob.witch.idle";
    }

    @Override
    public String getDeathSound() {
        return "mob.witch.death";
    }
}