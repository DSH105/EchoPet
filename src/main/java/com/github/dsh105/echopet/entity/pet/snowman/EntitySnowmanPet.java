package com.github.dsh105.echopet.entity.pet.snowman;

import net.minecraft.server.v1_6_R2.World;

import com.github.dsh105.echopet.entity.pet.EntityPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.SizeCategory;

public class EntitySnowmanPet extends EntityPet {
	
	public EntitySnowmanPet(World world, Pet pet) {
		super(world, pet);
		this.a(0.4F, 1.8F);
		this.fireProof = true;
	}

	@Override
	protected String r() {
		return "none";
	}
	
	@Override
	protected String aO() {
		return "none";
	}
	
	@Override
	public SizeCategory getSizeCategory() {
		return SizeCategory.REGULAR;
	}
}