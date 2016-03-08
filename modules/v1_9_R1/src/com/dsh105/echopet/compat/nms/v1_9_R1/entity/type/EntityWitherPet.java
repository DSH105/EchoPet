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

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityWitherPet;
import com.dsh105.echopet.compat.nms.v1_9_R1.entity.EntityPet;

import net.minecraft.server.v1_9_R1.*;

@EntitySize(width = 0.9F, height = 4.0F)
@EntityPetType(petType = PetType.WITHER)
public class EntityWitherPet extends EntityPet implements IEntityWitherPet {

	private static final DataWatcherObject<Integer> a = DataWatcher.a(EntityWither.class, DataWatcherRegistry.b);
	private static final DataWatcherObject<Integer> b = DataWatcher.a(EntityWither.class, DataWatcherRegistry.b);
	private static final DataWatcherObject<Integer> c = DataWatcher.a(EntityWither.class, DataWatcherRegistry.b);
	private static final DataWatcherObject<Integer> bw = DataWatcher.a(EntityWither.class, DataWatcherRegistry.b);
    public EntityWitherPet(World world) {
        super(world);
    }

    public EntityWitherPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
		this.datawatcher.register(a, Integer.valueOf(0));
		this.datawatcher.register(b, Integer.valueOf(0));
		this.datawatcher.register(c, Integer.valueOf(0));
		this.datawatcher.register(bw, Integer.valueOf(0));
    }

    public void setShielded(boolean flag) {
		this.datawatcher.set(bw, Integer.valueOf(flag ? 1 : 0));
        this.setHealth((float) (flag ? 150 : 300));
    }

    @Override
	protected SoundEffect getIdleSound(){
		return SoundEffects.gE;
    }

    @Override
	protected SoundEffect getDeathSound(){
		return SoundEffects.gG;
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.LARGE;
    }
}
