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

package com.dsh105.echopet.nms.v1_7_R3.entity;

import com.dsh105.echopet.api.entity.entitypet.EntityAgeablePet;
import com.dsh105.echopet.api.entity.pet.AgeablePet;
import net.minecraft.server.v1_7_R3.World;

public abstract class EntityAgeablePetBase<T extends AgeablePet> extends EntityPetBase<T> implements EntityAgeablePet<T> {

    public EntityAgeablePetBase(World world, T pet) {
        super(world, pet);
    }

    @Override
    public void setBaby(boolean flag) {
        if (flag) {
            this.datawatcher.watch(12, Integer.MIN_VALUE);
        } else {
            this.datawatcher.watch(12, 0);
        }
    }

    @Override
    public boolean isBaby() {
        return this.datawatcher.getInt(12) < 0;
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(12, new Integer(0));
    }
}