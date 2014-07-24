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

package com.dsh105.echopet.nms.v1_7_R3.entity.type;

import com.dsh105.echopet.api.entity.entitypet.type.EntityIronGolemPet;
import com.dsh105.echopet.api.entity.pet.type.IronGolemPet;
import com.dsh105.echopet.nms.v1_7_R3.entity.EntityPetBase;
import net.minecraft.server.v1_7_R3.World;
import org.bukkit.entity.LivingEntity;

public class EntityIronGolemPetBase extends EntityPetBase<IronGolemPet> implements EntityIronGolemPet {

    public EntityIronGolemPetBase(World world, IronGolemPet pet) {
        super(world, pet);
    }

    @Override
    public boolean attack(LivingEntity entity) {
        boolean flag = super.attack(entity);
        if (flag) {
            this.world.broadcastEntityEffect(this, (byte) 4);
            entity.setVelocity(entity.getVelocity().clone().setY(0.4000000059604645D));
            this.makeSound("mob.irongolem.throw", 1.0F, 1.0F);
        }
        return flag;
    }
}