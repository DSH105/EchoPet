package com.github.dsh105.echopet.entity.pet.cavespider;

import net.minecraft.server.v1_6_R2.World;

import com.github.dsh105.echopet.entity.pet.EntityPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.SizeCategory;

public class EntityCaveSpiderPet extends EntityPet {
	
	public EntityCaveSpiderPet(World world, Pet pet) {
		super(world, pet);
		this.a(0.7F, 0.5F);
		this.fireProof = true;
	}
	
	protected void a() {
		super.a();
        this.datawatcher.a(16, new Byte((byte) 0));
	}
	
	protected void a(int i, int j, int k, int l) {
		makeSound("mob.spider.step", 0.15F, 1.0F);
	}

	@Override
	protected String r() {
		return "mob.spider.say";
	}
	
	@Override
	protected String aO() {
		return "mob.spider.death";
	}
	
	@Override
	public SizeCategory getSizeCategory() {
		return SizeCategory.REGULAR;
	}
}
