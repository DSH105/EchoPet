package com.github.dsh105.echopet.entity.pet.creeper;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.util.Particle;
import net.minecraft.server.v1_6_R2.World;

import com.github.dsh105.echopet.entity.pet.EntityPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.SizeCategory;

public class EntityCreeperPet extends EntityPet {

	public EntityCreeperPet(World world) {
		super(world);
	}

	public EntityCreeperPet(World world, Pet pet) {
		super(world, pet);
		this.a(0.6F, 0.9F);
		this.fireProof = true;
	}
	
	public void setPowered(boolean flag) {
		this.datawatcher.watch(17, Byte.valueOf((byte) (flag ? 1 : 0)));
		((CreeperPet) pet).powered = flag;
	}
	
	//Obfuscated...
	
	protected void a() {
		super.a();
		this.datawatcher.a(16, Byte.valueOf((byte) -1));
		this.datawatcher.a(17, Byte.valueOf((byte) 0));
	}
	
	@Override
	protected String r() {
		return "";
    }
	
	@Override
	protected String aO() {
		return "mob.creeper.death";
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
				Particle.SMOKE.sendToLocation(pet.getLocation());
			} catch (Exception e) {
				EchoPet.getPluginInstance().debug(e, "Particle effect failed.");
			}
		}
	}
}
