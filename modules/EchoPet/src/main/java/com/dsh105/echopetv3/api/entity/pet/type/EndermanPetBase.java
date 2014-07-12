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

package com.dsh105.echopetv3.api.entity.pet.type;

import com.dsh105.echopetv3.api.entity.*;
import com.dsh105.echopetv3.api.entity.entitypet.type.EntityEndermanPet;
import com.dsh105.echopetv3.api.entity.pet.PetBase;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;

@PetInfo(type = PetType.ENDERMAN, width = 0.6F, height = 2.9F)
public class EndermanPetBase extends PetBase<Enderman, EntityEndermanPet> implements EndermanPet {

    public EndermanPetBase(Player owner) {
        super(owner);
    }

    @AttributeHandler(data = PetData.SCREAMING)
    @Override
    public void setScreaming(boolean flag) {
        getEntity().setScreaming(flag);
    }

    @AttributeHandler(data = PetData.SCREAMING, getter = true)
    @Override
    public boolean isScreaming() {
        return getEntity().isScreaming();
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.REGULAR;
    }

    @Override
    public String getIdleSound() {
        return isScreaming() ? "mob.endermen.scream" : "mob.endermen.idle";
    }

    @Override
    public String getDeathSound() {
        return "mob.endermen.death";
    }
}