package io.github.dsh105.echopet.entity.pet.cow;

import net.minecraft.server.v1_6_R3.*;

import io.github.dsh105.echopet.entity.pet.EntityAgeablePet;
import io.github.dsh105.echopet.entity.pet.Pet;


public class EntityCowPet extends EntityAgeablePet {

	public EntityCowPet(World world) {
		super(world);
	}

	public EntityCowPet(World world, Pet pet) {
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
		((CowPet) pet).baby = flag;
	}

	@Override
	protected void makeStepSound() {
        this.makeSound("mob.cow.step", 0.15F, 1.0F);
    }

	@Override
	protected String getIdleSound() {
		return "mob.cow.say";
	}
	
	@Override
	protected String getDeathSound() {
		return "mob.cow.hurt";
	}
}
