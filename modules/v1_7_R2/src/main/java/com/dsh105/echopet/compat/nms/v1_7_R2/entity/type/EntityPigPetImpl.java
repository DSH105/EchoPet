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

package com.dsh105.echopet.compat.nms.v1_7_R2.entity.type;

import com.dsh105.echopet.compat.api.entity.pet.Pet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.nms.type.EntityPigPet;
import com.dsh105.echopet.compat.nms.v1_7_R2.entity.EntityAgeablePetImpl;
import net.minecraft.server.v1_7_R2.World;

@EntitySize(width = 0.9F, height = 0.9F)
@EntityPetData(petType = PetType.PIG)
public class EntityPigPetImpl extends EntityAgeablePetImpl implements EntityPigPet {

    public EntityPigPetImpl(World world) {
        super(world);
    }

    public EntityPigPetImpl(World world, Pet pet) {
        super(world, pet);
    }

    public boolean hasSaddle() {
        return (this.datawatcher.getByte(16) & 1) != 0;
    }

    @Override
    public void setSaddled(boolean flag) {
        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) 1));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) 0));
        }
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
    }

    @Override
    protected void makeStepSound() {
        this.makeSound("mob.pig.step", 0.15F, 1.0F);
    }

    @Override
    protected String getIdleSound() {
        return "mob.pig.say";
    }

    @Override
    protected String getDeathSound() {
        return "mob.pig.death";
    }
}