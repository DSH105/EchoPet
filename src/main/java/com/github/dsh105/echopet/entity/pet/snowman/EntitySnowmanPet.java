package com.github.dsh105.echopet.entity.pet.snowman;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.util.Particle;
import net.minecraft.server.v1_6_R2.World;

import com.github.dsh105.echopet.entity.pet.EntityPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.SizeCategory;

public class EntitySnowmanPet extends EntityPet {

	public EntitySnowmanPet(World world) {
		super(world);
	}

	public EntitySnowmanPet(World world, Pet pet) {
		super(world, pet);
		this.a(0.4F, 1.8F);
		this.fireProof = true;
	}

	@Override
	protected String getIdleSound() {
		return "none";
	}
	
	@Override
	protected String getDeathSound() {
		return "none";
	}
	
	@Override
	public SizeCategory getSizeCategory() {
		return SizeCategory.REGULAR;
	}

	@Override
	public void l_() {
		super.l_();
		if (this.random.nextBoolean() && particle <= 0 && !this.isInvisible()) {
			try {
				Particle.SNOW_SHOVEL.sendToLocation(pet.getLocation());
			} catch (Exception e) {
				EchoPet.getPluginInstance().debug(e, "Particle effect failed.");
			}
		}
	}
}