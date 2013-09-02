package com.github.dsh105.echopet.entity.pet.mushroomcow;

import net.minecraft.server.v1_6_R2.World;

import com.github.dsh105.echopet.entity.pet.EntityAgeablePet;
import com.github.dsh105.echopet.entity.pet.Pet;


public class EntityMushroomCowPet extends EntityAgeablePet {

	public EntityMushroomCowPet(World world) {
		super(world);
	}

	public EntityMushroomCowPet(World world, Pet pet) {
		super(world, pet);
		this.a(0.9F, 1.3F);
		this.fireProof = true;
	}
	
	public void setBaby(boolean flag) {
		if (flag) {
			this.datawatcher.watch(12, Integer.valueOf(Integer.MIN_VALUE));
		} else {
			this.datawatcher.watch(12, new Integer(0));
		}
		((MushroomCowPet) pet).baby = flag;
	}
	
	protected void a() {
		super.a();
		//this.datawatcher.a(12, new Integer(0));
	}
	
	protected void a(int i, int j, int k, int l) {
        this.makeSound("mob.cow.step", 0.15F, 1.0F);
    }

	@Override
	protected String getIdleSound() {
		return "mob.cow.say";
	}
	
	@Override
	protected String getDeathSound() {
		return "mob.cow.death";
	}
}