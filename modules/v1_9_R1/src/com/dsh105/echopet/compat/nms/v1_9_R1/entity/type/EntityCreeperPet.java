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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityCreeperPet;
import com.dsh105.echopet.compat.nms.v1_9_R1.entity.EntityPet;

import net.minecraft.server.v1_9_R1.*;

@EntitySize(width = 0.6F, height = 1.9F)
@EntityPetType(petType = PetType.CREEPER)
public class EntityCreeperPet extends EntityPet implements IEntityCreeperPet {

	private static final DataWatcherObject<Integer> a = DataWatcher.a(EntityCreeper.class, DataWatcherRegistry.b);
	private static final DataWatcherObject<Boolean> b = DataWatcher.a(EntityCreeper.class, DataWatcherRegistry.h);
	private static final DataWatcherObject<Boolean> c = DataWatcher.a(EntityCreeper.class, DataWatcherRegistry.h);

    public EntityCreeperPet(World world) {
        super(world);
    }

    public EntityCreeperPet(World world, IPet pet) {
        super(world, pet);
    }

    
    public void setPowered(boolean flag) {
		this.datawatcher.set(b, Boolean.valueOf(true));
	}

    
    public void setIgnited(boolean flag) {
		this.datawatcher.set(c, Boolean.valueOf(true));
    }

    
    protected void initDatawatcher() {
        super.initDatawatcher();
		this.datawatcher.register(a, Integer.valueOf(-1));
		this.datawatcher.register(b, Boolean.valueOf(false));
		this.datawatcher.register(c, Boolean.valueOf(false));
    }

    
	protected SoundEffect getIdleSound(){
		return SoundEffects.as;
    }

    
	protected SoundEffect getDeathSound(){
		return SoundEffects.ar;
    }

    
    public SizeCategory getSizeCategory() {
        return SizeCategory.REGULAR;
    }
}
