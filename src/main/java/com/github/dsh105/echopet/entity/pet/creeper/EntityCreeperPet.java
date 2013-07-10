package com.github.dsh105.echopet.entity.pet.creeper;

import net.minecraft.server.v1_6_R2.World;

import com.github.dsh105.echopet.entity.pet.EntityPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.SizeCategory;

public class EntityCreeperPet extends EntityPet {
	
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
}
