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

package com.dsh105.echopet.nms.v1_7_R2.entity.type;

import com.dsh105.echopet.api.entity.entitypet.type.EntitySheepPet;
import com.dsh105.echopet.api.entity.pet.type.SheepPet;
import com.dsh105.echopet.nms.v1_7_R2.entity.EntityAgeablePetBase;
import net.minecraft.server.v1_7_R2.World;
import org.bukkit.DyeColor;

public class EntitySheepPetBase extends EntityAgeablePetBase<SheepPet> implements EntitySheepPet {

    public EntitySheepPetBase(World world, SheepPet pet) {
        super(world, pet);
    }

    @Override
    public void setSheared(boolean flag) {
        byte value = this.datawatcher.getByte(DATAWATCHER_SKIN);

        if (flag) {
            this.datawatcher.watch(DATAWATCHER_SKIN, (byte) (value | 16));
        } else {
            this.datawatcher.watch(DATAWATCHER_SKIN, (byte) (value & -17));
        }
    }

    @Override
    public boolean isSheared() {
        return (this.datawatcher.getByte(DATAWATCHER_SKIN) & 16) != 0;
    }

    @Override
    public DyeColor getDyeColor() {
        return DyeColor.getByWoolData((byte) (this.datawatcher.getByte(DATAWATCHER_SKIN) & 15));
    }

    @Override
    public void setDyeColor(DyeColor color) {
        byte value = this.datawatcher.getByte(16);
        this.datawatcher.watch(DATAWATCHER_SKIN, (byte) (value & 240 | color.getWoolData() & 15));
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(DATAWATCHER_SKIN, new Byte((byte) 0));
    }
}