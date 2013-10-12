package com.github.dsh105.echopet.entity.pet.cavespider;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.logger.Logger;
import com.github.dsh105.echopet.util.Particle;
import net.minecraft.server.v1_6_R3.*;

import com.github.dsh105.echopet.entity.pet.EntityPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.SizeCategory;

public class EntityCaveSpiderPet extends EntityPet {

	EntityCaveSpiderPet(World world) {
		super(world);
	}

	public EntityCaveSpiderPet(World world, Pet pet) {
		super(world, pet);
		this.a(0.7F, 0.5F);
		this.fireProof = true;
	}

	@Override
	protected void initDatawatcher() {
		super.initDatawatcher();
        this.datawatcher.a(16, new Byte((byte) 0));
	}

	@Override
	protected void makeStepSound() {
		makeSound("mob.spider.step", 0.15F, 1.0F);
	}

	@Override
	protected String getIdleSound() {
		return "mob.spider.say";
	}
	
	@Override
	protected String getDeathSound() {
		return "mob.spider.death";
	}
	
	@Override
	public SizeCategory getSizeCategory() {
		return SizeCategory.REGULAR;
	}

	@Override
	public void onLive() {
		super.onLive();
		if (this.random.nextBoolean() && particle <= 0 && !this.isInvisible()) {
			try {
				Particle.SPELL_AMBIENT.sendToLocation(pet.getLocation());
			} catch (Exception e) {
				Logger.log(Logger.LogLevel.WARNING, "Particle effect creation failed.", e, true);
			}
		}
	}
}
