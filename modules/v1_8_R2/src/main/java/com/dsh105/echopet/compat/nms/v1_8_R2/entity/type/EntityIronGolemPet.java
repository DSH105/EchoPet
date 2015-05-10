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

package com.dsh105.echopet.compat.nms.v1_8_R2.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityIronGolemPet;
import com.dsh105.echopet.compat.nms.v1_8_R2.entity.EntityPet;
import net.minecraft.server.v1_8_R2.Entity;
import net.minecraft.server.v1_8_R2.World;

@EntitySize(width = 1.4F, height = 2.9F)
@EntityPetType(petType = PetType.IRONGOLEM)
public class EntityIronGolemPet extends EntityPet implements IEntityIronGolemPet {

    public EntityIronGolemPet(World world) {
        super(world);
    }

    public EntityIronGolemPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
    }

    @Override
    public boolean attack(Entity entity) {
        boolean flag = super.attack(entity);
        if (flag) {
            this.world.broadcastEntityEffect(this, (byte) 4);
            entity.motY = 0.4000000059604645D;
            this.makeSound("mob.irongolem.throw", 1.0F, 1.0F);
        }
        return flag;
    }

    @Override
    protected void makeStepSound() {
        this.makeSound("mob.irongolem.walk", 1.0F, 1.0F);
    }

    @Override
    protected String getIdleSound() {
        return "none";
    }

    @Override
    protected String getDeathSound() {
        return "mob.irongolem.death";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.LARGE;
    }
}
