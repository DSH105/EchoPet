package com.github.dsh105.echopet.entity.pet.irongolem;

import net.minecraft.server.v1_6_R2.World;

import com.github.dsh105.echopet.entity.pet.EntityPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.SizeCategory;

public class EntityIronGolemPet extends EntityPet {
	
	public EntityIronGolemPet(World world, Pet pet) {
		super(world, pet);
		this.a(1.4F, 1.9F);
		this.fireProof = true;
	}

	protected void a() {
		super.a();
		this.datawatcher.a(16, Byte.valueOf((byte) 0));
	}

	protected void a(int i, int j, int k, int l) {
		this.makeSound("mob.irongolem.walk", 1.0F, 1.0F);
	}

	@Override
	protected String r() {
		return "none";
	}
	
	@Override
	protected String aO() {
		return "mob.irongolem.death";
	}
	
	@Override
	public SizeCategory getSizeCategory() {
		return SizeCategory.LARGE;
	}
}