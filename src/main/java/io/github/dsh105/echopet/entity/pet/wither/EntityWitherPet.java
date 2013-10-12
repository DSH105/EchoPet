package io.github.dsh105.echopet.entity.pet.wither;

import io.github.dsh105.echopet.EchoPet;
import io.github.dsh105.echopet.logger.Logger;
import io.github.dsh105.echopet.util.Particle;
import net.minecraft.server.v1_6_R3.*;

import io.github.dsh105.echopet.entity.pet.EntityPet;
import io.github.dsh105.echopet.entity.pet.Pet;
import io.github.dsh105.echopet.entity.pet.SizeCategory;

public class EntityWitherPet extends EntityPet {

	public EntityWitherPet(World world) {
		super(world);
	}

	public EntityWitherPet(World world, Pet pet) {
		super(world, pet);
		this.a(0.9F, 4.0F);
		this.fireProof = true;
	}

	protected void initDatawatcher() {
		super.initDatawatcher();
		this.datawatcher.a(17, new Integer(0));
		this.datawatcher.a(18, new Integer(0));
		this.datawatcher.a(19, new Integer(0));
		this.datawatcher.a(20, new Integer(0));
	}
	
	public void setShielded(boolean flag) {
		this.datawatcher.watch(20, new Integer((flag ? 1 : 0)));
		this.setHealth((float) (flag ? 150 : 300));
		((WitherPet) pet).shield = flag;
	}

	@Override
	protected String getIdleSound() {
		return "mob.wither.idle";
	}
	
	@Override
	protected String getDeathSound() {
		return "mob.wither.death";
	}
	
	@Override
	public SizeCategory getSizeCategory() {
		return SizeCategory.LARGE;
	}

	@Override
	public void onLive() {
		super.onLive();
		if (this.random.nextBoolean() && particle <= 0 && !this.isInvisible()) {
			try {
				Particle.VOID.sendToLocation(pet.getLocation());
			} catch (Exception e) {
				Logger.log(Logger.LogLevel.WARNING, "Particle effect creation failed.", e, true);
			}
		}
	}
}