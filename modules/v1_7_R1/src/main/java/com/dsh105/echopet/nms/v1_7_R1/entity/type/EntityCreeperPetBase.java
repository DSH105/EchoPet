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

package com.dsh105.echopet.nms.v1_7_R1.entity.type;

import com.dsh105.echopet.api.entity.entitypet.type.EntityCreeperPet;
import com.dsh105.echopet.api.entity.pet.type.CreeperPet;
import com.dsh105.echopet.nms.v1_7_R1.entity.EntityPetBase;
import net.minecraft.server.v1_7_R1.World;

public class EntityCreeperPetBase extends EntityPetBase<CreeperPet> implements EntityCreeperPet {

    public EntityCreeperPetBase(World world, CreeperPet pet) {
        super(world, pet);
    }

    @Override
    public void setPowered(boolean flag) {
        this.datawatcher.watch(DATAWATCHER_POWERED, (byte) (flag ? 1 : 0));
    }

    @Override
    public boolean isPowered() {
        return this.datawatcher.getByte(DATAWATCHER_POWERED) > 0;
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(DATAWATCHER_FUSE_STATE, Byte.valueOf((byte) -1));
        this.datawatcher.a(DATAWATCHER_POWERED, Byte.valueOf((byte) 0));
        this.datawatcher.a(DATAWATCHER_IGNITION, Byte.valueOf((byte) 0));
    }
}