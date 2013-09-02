package com.github.dsh105.echopet.entity.pet.chicken;

import net.minecraft.server.v1_6_R2.World;

import com.github.dsh105.echopet.entity.pet.EntityAgeablePet;
import com.github.dsh105.echopet.entity.pet.Pet;

public class EntityChickenPet extends EntityAgeablePet {

	public EntityChickenPet(World world) {
		super(world);
	}

	public EntityChickenPet(World world, Pet pet) {
		super(world, pet);
		this.a(0.3F, 0.7F);
		this.fireProof = true;
	}
	
	public void setBaby(boolean flag) {
		if (flag) {
			this.datawatcher.watch(12, Integer.valueOf(Integer.MIN_VALUE));
		} else {
			this.datawatcher.watch(12, new Integer(0));
		}
		((ChickenPet) pet).baby = flag;
	}
	
	protected void a() {
		super.a();
		this.datawatcher.watch(12, new Integer(0));
	}
	
	protected void a(int i, int j, int k, int l) {
		this.makeSound("mob.chicken.step", 0.15F, 1.0F);
	}
	
	@Override
	protected String getIdleSound() {
		return "mob.chicken.say";
	}
	
	@Override
	protected String getDeathSound() {
		return "mob.chicken.hurt";
	}
}
