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

package com.dsh105.echopet.compat.nms.v1_9_R1.entity;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.SizeCategory;

import net.minecraft.server.v1_9_R1.*;

public abstract class EntityAgeablePet extends EntityPet {

	private static final DataWatcherObject<Boolean> bv = DataWatcher.a(EntityAgeable.class, DataWatcherRegistry.h);
	protected int a;
    private boolean ageLocked = true;

    public EntityAgeablePet(World world) {
        super(world);
    }

    public EntityAgeablePet(World world, IPet pet) {
        super(world, pet);
    }

    public int getAge() {
		return ((Boolean) this.datawatcher.get(bv)).booleanValue() ? -1 : this.a;
    }

    public void setAge(int i) {
		a = (i < 0 ? -1 : (i >= 6000 ? 1 : 0));
		this.datawatcher.set(bv, a < 0);
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
		this.datawatcher.register(bv, false);
    }

    @Override
    public void m() {
        super.m();
        if (!(this.world.isClientSide || this.ageLocked)) {
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
		this.datawatcher.set(bv, flag);
    }

    @Override
    public boolean isBaby() {
		return this.datawatcher.get(bv).booleanValue();
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
