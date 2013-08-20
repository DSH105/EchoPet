package com.github.dsh105.echopet.entity.pet.horse;

import net.minecraft.server.v1_6_R2.Block;
import net.minecraft.server.v1_6_R2.StepSound;
import net.minecraft.server.v1_6_R2.World;

import com.github.dsh105.echopet.entity.pet.EntityAgeablePet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.SizeCategory;

public class EntityHorsePet extends EntityAgeablePet {
	
	int bP = 0;

	public EntityHorsePet(World world) {
		super(world);
	}

	public EntityHorsePet(World world, Pet pet) {
		super(world, pet);
		this.a(1.4F, 1.6F);
		this.fireProof = true;
	}
	
	public void setBaby(boolean flag) {
		if (flag) {
			this.datawatcher.watch(12, Integer.valueOf(-24000));
		} else {
			this.datawatcher.watch(12, new Integer(0));
		}
		((HorsePet) pet).baby = flag;
	}

	public boolean isBaby() {
		return ((HorsePet) pet).isBaby();
	}

	public void setSaddled(boolean flag) {
		this.b(4, flag);
		((HorsePet) pet).saddle = flag;
	}

	public void setType(HorseType t) {
		if (t != HorseType.NORMAL) {
			this.setArmour(HorseArmour.NONE);
		}
		this.datawatcher.watch(19, Byte.valueOf((byte) t.getId()));
		((HorsePet) pet).horseType = t;
	}

	public void setVariant(HorseVariant v, HorseMarking m) {
		this.datawatcher.watch(20, Integer.valueOf(m.getId(v)));
		((HorsePet) pet).variant = v;
		((HorsePet) pet).marking = m;
	}
	
	public void setArmour(HorseArmour a) {
		if (this.datawatcher.getByte(19) == Byte.valueOf((byte) HorseType.NORMAL.getId())) {
			this.datawatcher.watch(22, Integer.valueOf(a.getId()));
			((HorsePet) pet).armour = a;
		}
	}

	public void setChested(boolean flag) {
		this.b(8, flag);
	}

	private void b(int i, boolean flag) {
		int j = this.datawatcher.getInt(16);

		if (flag) {
			this.datawatcher.watch(16, Integer.valueOf(j | i));
		} else {
			this.datawatcher.watch(16, Integer.valueOf(j & ~i));
		}
	}

	public int getType() {
		return this.datawatcher.getByte(19);
	}

	protected void a() {
		super.a();
		this.datawatcher.a(16, Integer.valueOf(0));
		this.datawatcher.a(19, Byte.valueOf((byte) 0));
		this.datawatcher.a(20, Integer.valueOf(0));
		this.datawatcher.a(21, String.valueOf(""));
		this.datawatcher.a(22, Integer.valueOf(0));
	}

	@Override
	protected String r() {
		int i = this.getType();

		return i == 3 ? "mob.horse.zombie.idle" : (i == 4 ? "mob.horse.skeleton.idle" : (i != 1 && i != 2 ? "mob.horse.idle" : "mob.horse.donkey.idle"));
	}

	protected void a(int i, int j, int k, int l) {
		StepSound stepsound = Block.byId[l].stepSound;

		if (this.world.getTypeId(i, j + 1, k) == Block.SNOW.id) {
			stepsound = Block.SNOW.stepSound;
		}

		if (!Block.byId[l].material.isLiquid()) {
			int i1 = this.getType();

			if (this.passenger != null && i1 != 1 && i1 != 2) {
				++this.bP;
				if (this.bP > 5 && this.bP % 3 == 0) {
					this.makeSound("mob.horse.gallop", stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
					if (i1 == 0 && this.random.nextInt(10) == 0) {
						this.makeSound("mob.horse.breathe", stepsound.getVolume1() * 0.6F, stepsound.getVolume2());
					}
				} else if (this.bP <= 5) {
					this.makeSound("mob.horse.wood", stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
				}
			} else if (stepsound == Block.h) {
				this.makeSound("mob.horse.soft", stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
			} else {
				this.makeSound("mob.horse.wood", stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
			}
		}
	}

	@Override
	public void e(float f, float f1) {
		if (f1 <= 0.0F) {
			this.bP = 0;
		}
	}

	@Override
	protected String aO() {
		int i = this.getType();
		return i == 3 ? "mob.horse.zombie.death" : (i == 4 ? "mob.horse.skeleton.death" : (i != 1 && i != 2 ? "mob.horse.death" : "mob.horse.donkey.death"));
	}
	
	@Override
	public SizeCategory getSizeCategory() {
		if (this.isBaby()) {
			return SizeCategory.TINY;
		}
		else {
			return SizeCategory.LARGE;
		}
	}
}