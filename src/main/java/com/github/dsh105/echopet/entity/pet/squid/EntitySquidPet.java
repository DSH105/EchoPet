package com.github.dsh105.echopet.entity.pet.squid;

import net.minecraft.server.v1_6_R2.World;

import com.github.dsh105.echopet.entity.pet.EntityPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.SizeCategory;

public class EntitySquidPet extends EntityPet {
	
	//TODO: Make movement a lot smoother
	
	public EntitySquidPet(World world, Pet pet) {
		super(world, pet);
		this.a(0.95F, 0.95F);
		this.fireProof = true;
	}

	@Override
	protected String r() {
		return "";
	}
	
	@Override
	protected String aO() {
		return null;
	}
	
	@Override
	public SizeCategory getSizeCategory() {
		return SizeCategory.REGULAR;
	}
}