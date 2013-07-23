package com.github.dsh105.echopet.entity.pet.wither;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.util.Particle;
import net.minecraft.server.v1_6_R2.World;

import com.github.dsh105.echopet.entity.pet.EntityPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.SizeCategory;

public class EntityWitherPet extends EntityPet {

	public EntityWitherPet(World world, Pet pet) {
		super(world, pet);
		this.a(0.9F, 4.0F);
		this.fireProof = true;
	}

	protected void a() {
		super.a();
		this.datawatcher.a(16, new Integer(300));
		this.datawatcher.a(17, new Integer(0));
		this.datawatcher.a(18, new Integer(0));
		this.datawatcher.a(19, new Integer(0));
		this.datawatcher.a(20, new Integer(0));
	}
	
	public void setShielded(boolean flag) {
		this.datawatcher.watch(16, new Integer((flag ? 150 : 300)));
		((WitherPet) pet).shield = flag;
	}

	@Override
	protected String r() {
		return "mob.wither.idle";
	}
	
	@Override
	protected String aO() {
		return "mob.wither.death";
	}
	
	@Override
	public SizeCategory getSizeCategory() {
		return SizeCategory.LARGE;
	}

	@Override
	public void l_() {
		super.l_();
		if (this.random.nextBoolean()) {
			try {
				Particle.VOID.sendToLocation(pet.getLocation());
			} catch (Exception e) {
				EchoPet.getPluginInstance().debug(e, "Particle effect failed.");
			}
		}
	}
}