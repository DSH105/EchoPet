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

package com.dsh105.echopetv3.nms.v1_7_R2.entity.type;

import com.dsh105.echopetv3.api.entity.entitypet.type.EntityEndermanPet;
import com.dsh105.echopetv3.api.entity.pet.type.EndermanPet;
import com.dsh105.echopetv3.nms.v1_7_R2.entity.EntityPetBase;
import net.minecraft.server.v1_7_R2.World;

public class EntityEndermanPetBase extends EntityPetBase<EndermanPet> implements EntityEndermanPet {

    public EntityEndermanPetBase(World world, EndermanPet pet) {
        super(world, pet);
    }

    @Override
    public void setScreaming(boolean flag) {
        this.datawatcher.watch(DATAWATCHER_SCREAMING, (byte) (flag ? 1 : 0));
    }

    @Override
    public boolean isScreaming() {
        return this.datawatcher.getByte(DATAWATCHER_SCREAMING) > 0;
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(DATAWATCHER_CARRIED_BLOCK, new Byte((byte) 0));
        this.datawatcher.a(DATAWATCHER_CARRIED_BLOCK_DATA, new Byte((byte) 0));
        this.datawatcher.a(DATAWATCHER_SCREAMING, new Byte((byte) 0));
    }
}