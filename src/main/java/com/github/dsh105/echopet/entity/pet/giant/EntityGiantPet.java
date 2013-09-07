package com.github.dsh105.echopet.entity.pet.giant;

import com.github.dsh105.echopet.entity.pet.EntityPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.SizeCategory;
import net.minecraft.server.v1_6_R2.World;

/**
 * Project by DSH105
 */

public class EntityGiantPet extends EntityPet {

	public EntityGiantPet(World world) {
		super(world);
	}

	public EntityGiantPet(World world, Pet pet) {
		super(world, pet);
		this.height *= 6.0F;
		this.a(this.width * 5.0F, this.length * 5.0F);
	}

	protected void a(int i, int j, int k, int l) {
		this.makeSound("mob.zombie.step", 0.15F, 1.0F);
	}

	@Override
	protected String getIdleSound() {
		return "mob.zombie.say";
	}

	@Override
	protected String getDeathSound() {
		return "mob.zombie.death";
	}

	@Override
	public SizeCategory getSizeCategory() {
		return SizeCategory.OVERSIZE;
	}
}