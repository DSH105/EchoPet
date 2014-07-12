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

import com.dsh105.echopetv3.api.entity.PetInfo;
import com.dsh105.echopetv3.api.entity.PetType;
import com.dsh105.echopetv3.api.entity.entitypet.type.EntityCowPet;
import com.dsh105.echopetv3.api.entity.pet.AgeablePetBase;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;

@PetInfo(type = PetType.COW, width = 0.9F, height = 1.3F)
public class CowPetBase<T extends Cow, S extends EntityCowPet> extends AgeablePetBase<T, S> implements CowPet<T, S> {

    public CowPetBase(Player owner) {
        super(owner);
    }

    @Override
    public String getIdleSound() {
        return "mob.cow.say";
    }

    @Override
    public String getDeathSound() {
        return "mob.cow.hurt";
    }

    @Override
    public void makeStepSound() {
        getEntity().makeSound("mob.cow.step", 0.15F, 1.0F);
    }
}