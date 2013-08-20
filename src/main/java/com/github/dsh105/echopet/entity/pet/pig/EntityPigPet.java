package com.github.dsh105.echopet.entity.pet.pig;

import net.minecraft.server.v1_6_R2.World;

import com.github.dsh105.echopet.entity.pet.EntityAgeablePet;
import com.github.dsh105.echopet.entity.pet.Pet;

public class EntityPigPet extends EntityAgeablePet {

	public EntityPigPet(World world) {
		super(world);
	}

	public EntityPigPet(World world, Pet pet) {
		super(world, pet);
		this.a(0.9F, 0.9F);
		this.fireProof = true;
	}
	
	public void setBaby(boolean flag) {
		if (flag) {
			this.datawatcher.watch(12, Integer.valueOf(Integer.MIN_VALUE));
		} else {
			this.datawatcher.watch(12, new Integer(0));
		}
		((PigPet) pet).baby = flag;
	}
	
	public boolean hasSaddle() {
		return (this.datawatcher.getByte(16) & 1) != 0;
	}

	public void setSaddle(boolean flag) {
		if (flag) {
			this.datawatcher.watch(16, Byte.valueOf((byte) 1));
		}
		else {
			this.datawatcher.watch(16, Byte.valueOf((byte) 0));
		}
		((PigPet) pet).saddle = flag;
	}

	protected void a() {
		super.a();
		this.datawatcher.a(16, Byte.valueOf((byte) 0));
	}

	protected void a(int i, int j, int k, int l) {
		this.makeSound("mob.pig.step", 0.15F, 1.0F);
	}
	
	@Override
	protected String r() {
		return "mob.pig.say";
	}
	
	@Override
	protected String aO() {
		return "mob.pig.death";
	}
}