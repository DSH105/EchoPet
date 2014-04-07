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

package com.dsh105.echopet.compat.nms.v1_7_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityChickenPet;
import com.dsh105.echopet.compat.nms.v1_7_R1.entity.EntityAgeablePet;
import net.minecraft.server.v1_7_R1.World;

@EntitySize(width = 0.3F, height = 0.7F)
@EntityPetType(petType = PetType.CHICKEN)
public class EntityChickenPet extends EntityAgeablePet implements IEntityChickenPet {

    public EntityChickenPet(World world) {
        super(world);
    }

    public EntityChickenPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.watch(12, new Integer(0));
    }

    @Override
    protected void makeStepSound() {
        this.makeSound("mob.chicken.step", 0.15F, 1.0F);
    }

    @Override
    protected String getIdleSound() {
        return "mob.chicken.say";
    }

    @Override
    protected String getDeathSound() {
        return "mob.chicken.hurt";
    }
}
