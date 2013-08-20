package com.github.dsh105.echopet.entity.pet.silverfish;

import net.minecraft.server.v1_6_R2.World;

import com.github.dsh105.echopet.entity.pet.EntityPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.SizeCategory;

public class EntitySilverfishPet extends EntityPet {

	public EntitySilverfishPet(World world) {
		super(world);
	}

	public EntitySilverfishPet(World world, Pet pet) {
		super(world, pet);
		this.a(0.3F, 0.7F);
		this.fireProof = true;
	}

	protected void a(int i, int j, int k, int l) {
		this.makeSound("mob.silverfish.step", 0.15F, 1.0F);
	}

	@Override
	protected String r() {
		return "mob.silverfish.say";
	}
	
	@Override
	protected String aO() {
		return "mob.silverfish.kill";
	}
	
	@Override
	public SizeCategory getSizeCategory() {
		return SizeCategory.TINY;
	}
}