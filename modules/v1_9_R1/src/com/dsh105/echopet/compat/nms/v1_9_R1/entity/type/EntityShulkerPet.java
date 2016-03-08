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
