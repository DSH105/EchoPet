package io.github.dsh105.echopet.entity.pet.blaze;

import io.github.dsh105.echopet.EchoPet;
import io.github.dsh105.echopet.logger.Logger;
import io.github.dsh105.echopet.util.Particle;
import net.minecraft.server.v1_6_R3.*;

import io.github.dsh105.echopet.entity.pet.EntityPet;
import io.github.dsh105.echopet.entity.pet.Pet;
import io.github.dsh105.echopet.entity.pet.SizeCategory;

public class EntityBlazePet extends EntityPet {

	public EntityBlazePet(World world) {
		super(world);
	}

	public EntityBlazePet(World world, Pet pet) {
		super(world, pet);
		this.a(0.6F, 0.7F);
		this.fireProof = true;
	}
	
	public void setOnFire(boolean flag) {
		this.datawatcher.watch(16, (byte) (flag ? 1 : 0));
		((BlazePet) pet).onFire = flag;
	}

	@Override
	protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, new Byte((byte) 0));
    }
	
	@Override
	protected String getIdleSound() {
        return "mob.blaze.breathe";
    }
	
	@Override
	protected String getDeathSound() {
		return "mob.blaze.death";
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
				Particle.FIRE.sendToLocation(pet.getLocation());
				Particle.SMOKE.sendToLocation(pet.getLocation());
			} catch (Exception e) {
				Logger.log(Logger.LogLevel.WARNING, "Particle effect creation failed.", e, true);
			}
		}
	}
}
