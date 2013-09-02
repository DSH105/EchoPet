package com.github.dsh105.echopet.entity.pet.ocelot;

import net.minecraft.server.v1_6_R2.World;

import org.bukkit.entity.Ocelot.Type;

import com.github.dsh105.echopet.entity.pet.EntityAgeablePet;
import com.github.dsh105.echopet.entity.pet.Pet;

public class EntityOcelotPet extends EntityAgeablePet {

	public EntityOcelotPet(World world) {
		super(world);
	}

	public EntityOcelotPet(World world, Pet pet) {
		super(world, pet);
		this.a(0.9F, 0.9F);
		this.fireProof = true;
	}
	
	public void setBaby(boolean flag) {
		if (flag) {
			this.datawatcher.watch(12, Integer.valueOf(Integer.MIN_VALUE));
		} else {
			this.datawatcher.watch(12, new Integer(0));
		}
		((OcelotPet) pet).baby = flag;
	}
	
	public int getCatType() {
		return this.datawatcher.getByte(18);
	}

	public void setCatType(int i) {
		this.datawatcher.watch(18, (byte) i);
		((OcelotPet) pet).type = Type.getType(i);
	}
	
	@Override
	protected void a() {
		super.a();
		//this.datawatcher.a(12, new Integer(0));
		this.datawatcher.a(16, new Byte((byte) 0));
		this.datawatcher.a(18, new Byte((byte) 0));
	}
	
	protected void a(int i, int j, int k, int l) {
		this.makeSound("mob.ozelot.step", 0.15F, 1.0F);
	}
	
	@Override
	protected String getIdleSound() {
		return (this.random.nextInt(4) == 0 ? "mob.cat.purreow" : "mob.cat.meow");
	}
	
	@Override
	protected String getDeathSound() {
		return "mob.cat.hitt";
	}
}