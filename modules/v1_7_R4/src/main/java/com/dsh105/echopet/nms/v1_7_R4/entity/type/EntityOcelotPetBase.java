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

package com.dsh105.echopet.nms.v1_7_R4.entity.type;

import com.dsh105.echopet.api.entity.entitypet.type.EntityOcelotPet;
import com.dsh105.echopet.api.entity.pet.type.OcelotPet;
import com.dsh105.echopet.nms.v1_7_R4.entity.EntityAgeablePetBase;
import net.minecraft.server.v1_7_R4.World;
import org.bukkit.entity.Ocelot;

public class EntityOcelotPetBase extends EntityAgeablePetBase<OcelotPet> implements EntityOcelotPet {

    public EntityOcelotPetBase(World world, OcelotPet pet) {
        super(world, pet);
    }

    @Override
    public Ocelot.Type getCatType() {
        return Ocelot.Type.getType(this.datawatcher.getByte(DATAWATCHER_TYPE));
    }

    @Override
    public void setCatType(Ocelot.Type type) {
        this.datawatcher.watch(DATAWATCHER_TYPE, (byte) type.getId());
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(DATAWATCHER_ENTITY_STATUS, new Byte((byte) 0));
        this.datawatcher.a(DATAWATCHER_OWNER_NAME, getPet().getOwnerName());
        this.datawatcher.a(DATAWATCHER_TYPE, new Byte((byte) 0));
    }
}