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

import com.dsh105.echopet.api.entity.entitypet.type.EntityWitherPet;
import com.dsh105.echopet.api.entity.pet.type.WitherPet;
import com.dsh105.echopet.nms.v1_7_R1.entity.EntityPetBase;
import net.minecraft.server.v1_7_R1.World;

public class EntityWitherPetBase extends EntityPetBase<WitherPet> implements EntityWitherPet {

    public EntityWitherPetBase(World world, WitherPet pet) {
        super(world, pet);
    }

    @Override
    public void setShielded(boolean flag) {
        this.datawatcher.watch(DATAWATCHER_INVULNERABLE_TIME, flag ? 1 : 0);
        setHealth((float) (flag ? 150 : 300));
    }

    @Override
    public boolean isShielded() {
        return this.datawatcher.getInt(DATAWATCHER_INVULNERABLE_TIME) == 1;
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(DATAWATCHER_INVULNERABLE_TIME, new Integer(0));
    }
}