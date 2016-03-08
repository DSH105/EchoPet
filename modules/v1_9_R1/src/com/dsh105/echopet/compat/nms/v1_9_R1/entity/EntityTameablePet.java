package com.dsh105.echopet.compat.nms.v1_9_R1.entity;

import java.util.UUID;

import com.dsh105.echopet.compat.api.entity.IEntityTameablePet;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.google.common.base.Optional;

import net.minecraft.server.v1_9_R1.*;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Mar 6, 2016
 */
// These are not actually tameable and only here to add missing datawatchers
public class EntityTameablePet extends EntityAgeablePet implements IEntityTameablePet{

	protected static final DataWatcherObject<Byte> bv = DataWatcher.a(EntityTameableAnimal.class, DataWatcherRegistry.a);
	protected static final DataWatcherObject<Optional<UUID>> bw = DataWatcher.a(EntityTameableAnimal.class, DataWatcherRegistry.m);

	public EntityTameablePet(World world){
		super(world);
	}

	public EntityTameablePet(World world, IPet pet){
		super(world, pet);
	}

	protected void i(){
		super.i();
		this.datawatcher.register(bv, Byte.valueOf((byte) 0));
		this.datawatcher.register(bw, Optional.absent());
	}

	@Override
	protected SoundEffect getIdleSound(){// wolf sounds
		return SoundEffects.gP;
	}

	@Override
	protected SoundEffect getDeathSound(){
		return SoundEffects.gL;
	}

	// These are all useless
	public boolean isTamed(){
		return (((Byte) this.datawatcher.get(bv)).byteValue() & 0x4) != 0;
	}

	public void setTamed(boolean paramBoolean){
		int i = ((Byte) this.datawatcher.get(bv)).byteValue();
		if(paramBoolean){
			this.datawatcher.set(bv, Byte.valueOf((byte) (i | 0x4)));
		}else{
			this.datawatcher.set(bv, Byte.valueOf((byte) (i & 0xFFFFFFFB)));
		}
	}

	public boolean isSitting(){
		return (((Byte) this.datawatcher.get(bv)).byteValue() & 0x1) != 0;
	}

	public void setSitting(boolean paramBoolean){
		int i = ((Byte) this.datawatcher.get(bv)).byteValue();
		if(paramBoolean){
			this.datawatcher.set(bv, Byte.valueOf((byte) (i | 0x1)));
		}else{
			this.datawatcher.set(bv, Byte.valueOf((byte) (i & 0xFFFFFFFE)));
		}
	}

	public UUID getOwnerUUID(){
		return (UUID) ((Optional) this.datawatcher.get(bw)).orNull();
	}

	public void setOwnerUUID(UUID paramUUID){
		this.datawatcher.set(bw, Optional.fromNullable(paramUUID));
	}
}
