package io.github.dsh105.echopet.entity.pet.ghast;

import net.minecraft.server.v1_6_R3.*;

import io.github.dsh105.echopet.entity.pet.EntityPet;
import io.github.dsh105.echopet.entity.pet.Pet;
import io.github.dsh105.echopet.entity.pet.SizeCategory;

public class EntityGhastPet extends EntityPet {

	public EntityGhastPet(World world) {
		super(world);
	}

	public EntityGhastPet(World world, Pet pet) {
		super(world, pet);
		this.a(4.0F, 4.0F);
		this.fireProof = true;
	}

	@Override
	protected String getIdleSound() {
		return "mob.ghast.moan";
	}
	
	@Override
	protected String getDeathSound() {
		return "mob.ghast.death";
	}
	
	@Override
	public SizeCategory getSizeCategory() {
		return SizeCategory.OVERSIZE;
	}
}