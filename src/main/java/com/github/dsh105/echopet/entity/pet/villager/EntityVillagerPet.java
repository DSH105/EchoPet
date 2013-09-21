package com.github.dsh105.echopet.entity.pet.villager;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.util.Particle;
import net.minecraft.server.v1_6_R3.*;

import org.bukkit.entity.Villager.Profession;

import com.github.dsh105.echopet.entity.pet.EntityAgeablePet;
import com.github.dsh105.echopet.entity.pet.Pet;

public class EntityVillagerPet extends EntityAgeablePet {

	public EntityVillagerPet(World world) {
		super(world);
	}

	public EntityVillagerPet(World world, Pet pet) {
		super(world, pet);
		this.a(0.6F, 1.8F);
		this.fireProof = true;
	}

	public void setProfession(int i) {
		this.datawatcher.watch(16, i);
		((VillagerPet) pet).profession = Profession.getProfession(i);
	}

	public void setBaby(boolean flag) {
		if (flag) {
			this.datawatcher.watch(12, Integer.valueOf(Integer.MIN_VALUE));
		} else {
			this.datawatcher.watch(12, new Integer(0));
		}
		((VillagerPet) pet).baby = flag;
	}
	
	@Override
	protected String getIdleSound() {
		return this.random.nextBoolean() ? "mob.villager.haggle" : "mob.villager.idle";
	}
	
	@Override
	protected String getDeathSound() {
		return "mob.villager.death";
	}

	@Override
	public void initDatawatcher() {
		super.initDatawatcher();
		this.datawatcher.a(16, new Integer(0));
	}

	@Override
	public void onLive() {
		super.onLive();
		if (this.random.nextBoolean() && particle <= 0 && !this.isInvisible()) {
			try {
				Particle.SPARKLE.sendToLocation(pet.getLocation());
			} catch (Exception e) {
				EchoPet.getPluginInstance().debug(e, "Particle effect failed.");
			}
		}
	}
}