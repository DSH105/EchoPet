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

import com.dsh105.echopetv3.api.entity.AttributeHandler;
import com.dsh105.echopetv3.api.entity.PetData;
import com.dsh105.echopetv3.api.entity.PetInfo;
import com.dsh105.echopetv3.api.entity.PetType;
import com.dsh105.echopetv3.api.entity.entitypet.type.EntityPigPet;
import com.dsh105.echopetv3.api.entity.pet.AgeablePetBase;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;

@PetInfo(type = PetType.PIG, width = 0.9F, height = 0.9F)
public class PigPetBase extends AgeablePetBase<Pig, EntityPigPet> implements PigPet {

    public PigPetBase(Player owner) {
        super(owner);
    }

    @AttributeHandler(data = PetData.SADDLE)
    @Override
    public void setSaddle(boolean flag) {
        getEntity().setSaddle(flag);
    }

    @AttributeHandler(data = PetData.SADDLE, getter = true)
    @Override
    public boolean hasSaddle() {
        return getEntity().hasSaddle();
    }

    @Override
    public String getIdleSound() {
        return "mob.pig.say";
    }

    @Override
    public String getDeathSound() {
        return "mob.pig.death";
    }

    @Override
    public void makeStepSound() {
        getEntity().makeSound("mob.pig.step", 0.15F, 1.0F);
    }
}