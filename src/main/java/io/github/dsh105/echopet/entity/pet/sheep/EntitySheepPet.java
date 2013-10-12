package io.github.dsh105.echopet.entity.pet.sheep;

import net.minecraft.server.v1_6_R3.*;

import io.github.dsh105.echopet.entity.pet.EntityAgeablePet;
import io.github.dsh105.echopet.entity.pet.Pet;


public class EntitySheepPet extends EntityAgeablePet {

	public EntitySheepPet(World world) {
		super(world);
	}

	public EntitySheepPet(World world, Pet pet) {
		super(world, pet);
		this.a(0.9F, 1.3F);
		this.fireProof = true;
	}
	
	public void setBaby(boolean flag) {
		if (flag) {
			this.datawatcher.watch(12, Integer.valueOf(Integer.MIN_VALUE));
		} else {
			this.datawatcher.watch(12, new Integer(0));
		}
		((SheepPet) pet).baby = flag;
	}
	
	public int getColor() {
        return this.datawatcher.getByte(16) & 15;
    }

    public void setColor(int i) {
        byte b0 = this.datawatcher.getByte(16);

        byte b = Byte.valueOf((byte) (b0 & 240 | i & 15));
        this.datawatcher.watch(16, b);
        ((SheepPet) pet).color = b;
    }

    public boolean isSheared() {
        return (this.datawatcher.getByte(16) & 16) != 0;
    }

    public void setSheared(boolean flag) {
        byte b0 = this.datawatcher.getByte(16);

        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 16)));
        }
        else {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & -17)));
        }
        ((SheepPet) pet).sheared = flag;
    }

	@Override
	protected void initDatawatcher() {
		super.initDatawatcher();
		this.datawatcher.a(16, new Byte((byte) 0));
	}

	@Override
	protected void makeStepSound() {
        this.makeSound("mob.sheep.step", 0.15F, 1.0F);
    }

	@Override
	protected String getIdleSound() {
		return "mob.sheep.say";
	}
	
	@Override
	protected String getDeathSound() {
		return "mob.sheep.say";
	}
}