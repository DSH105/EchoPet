package com.github.dsh105.echopet.entity.pet;

import net.minecraft.server.v1_6_R3.World;

public abstract class EntityAgeablePet extends EntityPet {

	public EntityAgeablePet(World world) {
		super(world);
	}

	public EntityAgeablePet(World world, Pet pet) {
		super(world, pet);
	}

	protected void initDatawatcher() {
		super.initDatawatcher();
		this.datawatcher.a(12, new Integer(0));
	}
	
	public abstract void setBaby(boolean flag);
	
	public boolean isBaby() {
		return this.datawatcher.getInt(12) < 0;
	}
	
	@Override
	public SizeCategory getSizeCategory() {
		if (this.isBaby()) {
			return SizeCategory.TINY;
		}
		else {
			return SizeCategory.REGULAR;
		}
	}
}