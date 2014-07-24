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

import com.dsh105.echopet.api.entity.entitypet.type.EntityZombiePet;
import com.dsh105.echopet.api.entity.pet.type.ZombiePet;
import com.dsh105.echopet.nms.v1_7_R3.entity.EntityEquipablePetBase;
import net.minecraft.server.v1_7_R3.World;

public class EntityZombiePetBase<T extends ZombiePet> extends EntityEquipablePetBase<T> implements EntityZombiePet<T> {

    public EntityZombiePetBase(World world, T pet) {
        super(world, pet);
    }

    @Override
    public void setVillager(boolean flag) {
        this.datawatcher.watch(DATAWATCHER_VILLAGER, (byte) (flag ? 1 : 0));
    }

    @Override
    public boolean isVillager() {
        return this.datawatcher.getByte(DATAWATCHER_VILLAGER) > 0;
    }

    @Override
    public void setBaby(boolean flag) {
        this.datawatcher.watch(DATAWATCHER_BABY, (byte) (flag ? 1 : 0));
    }

    @Override
    public boolean isBaby() {
        return this.datawatcher.getByte(DATAWATCHER_BABY) > 0;
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(DATAWATCHER_BABY, new Byte((byte) 0));
        this.datawatcher.a(DATAWATCHER_VILLAGER, new Byte((byte) 0));
    }
}