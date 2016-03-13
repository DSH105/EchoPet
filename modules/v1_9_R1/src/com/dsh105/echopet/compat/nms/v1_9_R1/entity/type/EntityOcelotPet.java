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

package com.dsh105.echopet.compat.nms.v1_9_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityOcelotPet;
import com.dsh105.echopet.compat.nms.v1_9_R1.entity.EntityTameablePet;

import net.minecraft.server.v1_9_R1.*;

@EntitySize(width = 0.6F, height = 0.8F)
@EntityPetType(petType = PetType.OCELOT)
public class EntityOcelotPet extends EntityTameablePet implements IEntityOcelotPet{

	private static final DataWatcherObject<Integer> bz = DataWatcher.a(EntityOcelot.class, DataWatcherRegistry.b);

    public EntityOcelotPet(World world) {
        super(world);
    }

    public EntityOcelotPet(World world, IPet pet) {
        super(world, pet);
    }

    public int getCatType() {
		return ((Integer) this.datawatcher.get(bz)).intValue();
    }

    @Override
    public void setCatType(int i) {
		this.datawatcher.set(bz, Integer.valueOf(i));
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
		this.datawatcher.register(bz, Integer.valueOf(0));
    }

    @Override
	protected SoundEffect getIdleSound(){
		return(this.random.nextInt(4) == 0 ? SoundEffects.U : SoundEffects.P);
    }

    @Override
	protected SoundEffect getDeathSound(){
		return SoundEffects.Q;
    }
}
