package com.github.dsh105.echopet.entity.pet.spider;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.util.Particle;
import net.minecraft.server.v1_6_R2.World;

import com.github.dsh105.echopet.entity.pet.EntityPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.SizeCategory;

public class EntitySpiderPet extends EntityPet {

	public EntitySpiderPet(World world, Pet pet) {
		super(world, pet);
		this.a(1.4F, 0.9F);
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
	protected String aO() {
		return "mob.spider.death";
	}
	
	@Override
	public SizeCategory getSizeCategory() {
		return SizeCategory.REGULAR;
	}

	@Override
	public void l_() {
		if (this.random.nextBoolean()) {
			try {
				Particle.SPELL_AMBIENT.sendToLocation(pet.getLocation());
			} catch (Exception e) {
				EchoPet.getPluginInstance().debug(e, "Particle effect failed.");
			}
		}
	}
}
