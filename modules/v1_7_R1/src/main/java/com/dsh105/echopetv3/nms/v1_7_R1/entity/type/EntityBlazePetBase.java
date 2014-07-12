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

package com.dsh105.echopetv3.nms.v1_7_R1.entity.type;

import com.dsh105.echopetv3.api.entity.entitypet.type.EntityBlazePet;
import com.dsh105.echopetv3.api.entity.pet.type.BlazePet;
import com.dsh105.echopetv3.nms.v1_7_R1.entity.EntityPetBase;
import net.minecraft.server.v1_7_R1.World;

public class EntityBlazePetBase extends EntityPetBase<BlazePet> implements EntityBlazePet {

    public EntityBlazePetBase(World world, BlazePet pet) {
        super(world, pet);
    }

    @Override
    public void setOnFire(boolean flag) {
        this.datawatcher.watch(DATAWATCHER_ON_FIRE, (byte) (flag ? 1 : 0));
    }

    @Override
    public boolean isOnFire() {
        return this.datawatcher.getByte(DATAWATCHER_ON_FIRE) == 1;
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(DATAWATCHER_ON_FIRE, new Byte((byte) 0));
    }
}