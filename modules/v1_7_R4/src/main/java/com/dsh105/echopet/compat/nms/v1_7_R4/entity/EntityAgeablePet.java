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

package com.dsh105.echopet.compat.nms.v1_7_R4.entity;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import net.minecraft.server.v1_7_R4.World;

public abstract class EntityAgeablePet extends EntityPet {

    private boolean ageLocked = true;

    public EntityAgeablePet(World world) {
        super(world);
    }

    public EntityAgeablePet(World world, IPet pet) {
        super(world, pet);
    }

    public int getAge() {
        return this.datawatcher.getByte(12);
    }

    public void setAge(int i) {
        this.datawatcher.watch(12, Byte.valueOf((byte) i));
    }

    public boolean isAgeLocked() {
        return ageLocked;
    }

    public void setAgeLocked(boolean ageLocked) {
        this.ageLocked = ageLocked;
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(12, new Byte((byte) 0));
    }

    @Override
    public void e() {
        super.e();
        if (!(this.world.isStatic || this.ageLocked)) {
            int i = this.getAge();

            if (i < 0) {
                ++i;
                this.setAge(i);
            } else if (i > 0) {
                --i;
                this.setAge(i);
            }
        }
    }

    public void setBaby(boolean flag) {
        if (flag) {
            this.datawatcher.watch(12, Byte.valueOf((byte) -1));
        } else {
            this.datawatcher.watch(12, new Byte((byte) 0));
        }
    }

    @Override
    public boolean isBaby() {
        return this.datawatcher.getInt(12) < 0;
    }

    @Override
    public SizeCategory getSizeCategory() {
        if (this.isBaby()) {
            return SizeCategory.TINY;
        } else {
            return SizeCategory.REGULAR;
        }
    }
}
