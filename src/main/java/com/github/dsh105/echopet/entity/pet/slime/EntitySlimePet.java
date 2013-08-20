package com.github.dsh105.echopet.entity.pet.slime;

import net.minecraft.server.v1_6_R2.World;

import com.github.dsh105.echopet.entity.pet.EntityPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.SizeCategory;

public class EntitySlimePet extends EntityPet {

	public EntitySlimePet(World world) {
		super(world);
	}

	int jumpDelay;
	
	public EntitySlimePet(World world, Pet pet) {
		super(world, pet);
		int i = 1 << this.random.nextInt(3);
		this.height = 0.0F;
		this.jumpDelay = this.random.nextInt(15) + 10;
		this.setSize(i);
		this.fireProof = true;
	}
	
	/*@Override
	public int getMaxHealth() {
		int i = this.getSize();
		return i * i;
	}*/
	
	protected void setSize(int i) {
		this.datawatcher.watch(16, new Byte((byte) i));
        this.a(0.6F * (float) i, 0.6F * (float) i);
        this.setPosition(this.locX, this.locY, this.locZ);
        this.setHealth(this.getMaxHealth());
        //this.be = i; //1.5.2
        ((SlimePet) pet).size = i;
	}

	public int getSize() {
		return this.datawatcher.getByte(16);
	}

	protected void a() {
		super.a();
		this.datawatcher.a(16, new Byte((byte) 1));
	}

	@Override
	protected String r() {
		return "";
	}
	
	protected String aO() {
		return "mob.slime." + (this.getSize() > 1 ? "big" : "small");
	}
	
	public void l_() {
		super.l_();
		
		if (this.onGround && this.jumpDelay-- <= 0) {
			this.jumpDelay = this.bL();
			if (this.bO()) {
				this.makeSound(this.aO(), this.aZ(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
			}
			getControllerJump().a();
		}
	}

	//q() - 1.5.2
	public boolean bO() {
		return this.getSize() > 0;
	}

	//j() - 1.5.2
	//bH() - 1.6.2
	protected int bL() {
		return this.random.nextInt(15) + 10;
	}
	
	@Override
	protected float aZ() {
		return 0.4F * (float) this.getSize();
	}
	
	@Override
	public SizeCategory getSizeCategory() {
		if (this.getSize() == 1) {
			return SizeCategory.TINY;
		}
		else if (this.getSize() == 4) {
			return SizeCategory.LARGE;
		}
		else {
			return SizeCategory.REGULAR;
		}
	}
}