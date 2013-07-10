package com.github.dsh105.echopet.entity.pet.witch;

import net.minecraft.server.v1_6_R2.World;

import com.github.dsh105.echopet.entity.pet.EntityPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.SizeCategory;

public class EntityWitchPet extends EntityPet {
	
	public EntityWitchPet(World world, Pet pet) {
		super(world, pet);
		this.a(0.6F, 0.8F);
		this.fireProof = true;
	}

	protected String r() {
		return "mob.witch.idle";
	}
	
	@Override
	protected String aO() {
		return "mob.witch.death";
	}
	
	@Override
	public SizeCategory getSizeCategory() {
		return SizeCategory.REGULAR;
	}
}