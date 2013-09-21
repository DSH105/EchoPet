package com.github.dsh105.echopet.entity.pet.squid;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.util.Particle;
import net.minecraft.server.v1_6_R3.*;

import com.github.dsh105.echopet.entity.pet.EntityPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.SizeCategory;

public class EntitySquidPet extends EntityPet {

	public EntitySquidPet(World world) {
		super(world);
	}

	public EntitySquidPet(World world, Pet pet) {
		super(world, pet);
		this.a(0.95F, 0.95F);
		this.fireProof = true;
	}

	@Override
	protected String getIdleSound() {
		return "";
	}
	
	@Override
	protected String getDeathSound() {
		return null;
	}
	
	@Override
	public SizeCategory getSizeCategory() {
		return SizeCategory.REGULAR;
	}

	@Override
	public void onLive() {
		super.onLive();
		if (this.random.nextBoolean() && particle <= 0 && !this.isInvisible()) {
			if (pet.getEntityPet().world.getMaterial((int) pet.getEntityPet().locX, (int) pet.getEntityPet().locY, (int) pet.getEntityPet().locZ).isLiquid()) {
				try {
					Particle.BUBBLE.sendToLocation(pet.getLocation());
				} catch (Exception e) {
					EchoPet.getPluginInstance().debug(e, "Particle effect failed.");
				}
			}
			try {
				Particle.SPLASH.sendToLocation(pet.getLocation());
			} catch (Exception e) {
				EchoPet.getPluginInstance().debug(e, "Particle effect failed.");
			}
		}
	}
}