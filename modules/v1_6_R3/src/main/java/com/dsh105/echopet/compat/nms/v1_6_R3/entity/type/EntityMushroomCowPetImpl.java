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

package com.dsh105.echopet.compat.nms.v1_6_R3.entity.type;

import com.dsh105.echopet.compat.api.entity.pet.Pet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.nms.type.EntityMushroomCowPet;
import com.dsh105.echopet.compat.nms.v1_6_R3.entity.EntityAgeablePetImpl;
import net.minecraft.server.v1_6_R3.World;

@EntitySize(width = 0.9F, height = 1.3F)
@EntityPetData(petType = PetType.MUSHROOMCOW)
public class EntityMushroomCowPetImpl extends EntityAgeablePetImpl implements EntityMushroomCowPet {

    public EntityMushroomCowPetImpl(World world) {
        super(world);
    }

    public EntityMushroomCowPetImpl(World world, Pet pet) {
        super(world, pet);
    }

    @Override
    protected void makeStepSound() {
        this.makeSound("mob.cow.step", 0.15F, 1.0F);
    }

    @Override
    protected String getIdleSound() {
        return "mob.cow.say";
    }

    @Override
    protected String getDeathSound() {
        return "mob.cow.death";
    }
}