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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityShulkerPet;
import com.dsh105.echopet.compat.nms.v1_9_R1.entity.EntityPet;
import com.google.common.base.Optional;

import net.minecraft.server.v1_9_R1.*;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Mar 7, 2016
 */
@EntitySize(width = 1.0F, height = 1.0F)
@EntityPetType(petType = PetType.SHULKER)
public class EntityShulkerPet extends EntityPet implements IEntityShulkerPet{// TODO: spawns, find a way to move it.

	protected static final DataWatcherObject<EnumDirection> a = DataWatcher.a(EntityShulker.class, DataWatcherRegistry.l);
	protected static final DataWatcherObject<Optional<BlockPosition>> b = DataWatcher.a(EntityShulker.class, DataWatcherRegistry.k);
	protected static final DataWatcherObject<Byte> c = DataWatcher.a(EntityShulker.class, DataWatcherRegistry.a);

	public EntityShulkerPet(World world){
		super(world);
	}

	public EntityShulkerPet(World world, IPet pet){
		super(world, pet);
	}

	@Override
	protected SoundEffect getIdleSound(){
		return SoundEffects.eT;
	}

	@Override
	protected SoundEffect getDeathSound(){
		return SoundEffects.eX;
	}

	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(a, EnumDirection.UP);
		this.datawatcher.register(b, Optional.absent());
		this.datawatcher.register(c, Byte.valueOf((byte) 0));
	}

	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
}
