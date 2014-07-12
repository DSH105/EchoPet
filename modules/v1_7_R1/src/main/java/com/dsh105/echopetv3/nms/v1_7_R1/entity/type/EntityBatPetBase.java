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

import com.dsh105.echopetv3.api.entity.entitypet.type.EntityBatPet;
import com.dsh105.echopetv3.api.entity.pet.type.BatPet;
import com.dsh105.echopetv3.nms.v1_7_R1.entity.EntityPetBase;
import net.minecraft.server.v1_7_R1.World;

public class EntityBatPetBase extends EntityPetBase<BatPet> implements EntityBatPet {

    public EntityBatPetBase(World world, BatPet pet) {
        super(world, pet);
    }

    @Override
    public void setStartled(boolean flag) {
        byte value = this.datawatcher.getByte(16);

        if (flag) {
            this.datawatcher.watch(DATAWATCHER_STARTLED, Byte.valueOf((byte) (value | 1)));
        } else {
            this.datawatcher.watch(DATAWATCHER_STARTLED, Byte.valueOf((byte) (value & -2)));
        }
    }

    @Override
    public boolean isStartled() {
        return (this.datawatcher.getByte(DATAWATCHER_STARTLED) & 1) != 0;
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(DATAWATCHER_STARTLED, new Byte((byte) 0));
    }
}