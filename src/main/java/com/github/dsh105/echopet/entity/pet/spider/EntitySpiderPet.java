package com.github.dsh105.echopet.entity.pet.spider;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.util.Particle;
import net.minecraft.server.v1_6_R3.*;

import com.github.dsh105.echopet.entity.pet.EntityPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.SizeCategory;

public class EntitySpiderPet extends EntityPet {

	public EntitySpiderPet(World world) {
		super(world);
	}

	public EntitySpiderPet(World world, Pet pet) {
		super(world, pet);
		this.a(1.4F, 0.9F);
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

	public void a(boolean flag) {
		byte b0 = this.datawatcher.getByte(16);

		if (flag) {
			b0 = (byte) (b0 | 1);
		} else {
			b0 &= -2;
		}

		this.datawatcher.watch(16, Byte.valueOf(b0));
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
				EchoPet.getPluginInstance().debug(e, "Particle effect failed.");
			}
		}
	}
}
