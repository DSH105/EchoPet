package com.github.dsh105.echopet.entity.pet.witch;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.util.Particle;
import net.minecraft.server.v1_6_R2.World;

import com.github.dsh105.echopet.entity.pet.EntityPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.SizeCategory;

public class EntityWitchPet extends EntityPet {

	public EntityWitchPet(World world) {
		super(world);
	}

	public EntityWitchPet(World world, Pet pet) {
		super(world, pet);
		this.a(0.6F, 0.8F);
		this.fireProof = true;
	}

	protected String getIdleSound() {
		return "mob.witch.idle";
	}
	
	@Override
	protected String getDeathSound() {
		return "mob.witch.death";
	}
	
	@Override
	public SizeCategory getSizeCategory() {
		return SizeCategory.REGULAR;
	}

	@Override
	public void l_() {
		super.l_();
		if (this.random.nextBoolean() && particle <= 0) {
			try {
				Particle.WITCH_MAGIC.sendToLocation(pet.getLocation());
			} catch (Exception e) {
				EchoPet.getPluginInstance().debug(e, "Particle effect failed.");
			}
		}
	}
}