package com.github.dsh105.echopet.entity.pet.human;
// May support human pets
// This may require a large amount of testing, especially getting the client to render certain skins

/*
package me.dsh105.echopet.entity.pet.human;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import net.minecraft.server.v1_6_R1.*;
import org.bukkit.craftbukkit.v1_6_R1.*;
import me.dsh105.echopet.entity.pet.EntityPet;
import me.dsh105.echopet.entity.pet.Pet;
public class EntityHumanPet extends EntityPet {
	
	public EntityHumanPet(World world, Pet pet) {
		super(world, pet);
		this.height = 1.62F;
		this.aK = "humanoid";
		this.aJ = 180.0F;
		this.fireProof = true;
	}

	public void e(float f, float f1) {
		double d0 = this.locX;
		double d1 = this.locY;
		double d2 = this.locZ;

		if (pet.getOwner().isFlying()) {
			double d3 = this.motY;
			float f2 = this.aP;
			super.e(f, f1);
			this.motY = d3 * 0.6D;
			this.aP = f2;
		} else {
			super.e(f, f1);
		}
	}

	protected void b(int i, boolean flag) {
		byte b0 = this.datawatcher.getByte(16);

		if (flag) {
			this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 1 << i)));
		} else {
			this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & ~(1 << i))));
		}
	}

	protected void a() {
		super.a();
		this.datawatcher.a(16, Byte.valueOf((byte) 0));
		this.datawatcher.a(17, Byte.valueOf((byte) 0));
		this.datawatcher.a(18, Integer.valueOf(0));
	}

	protected void a(int i, int j, int k, int l) {
		Location el = this.getLocation();
		Block b = new Location(el.getWorld(), el.getX(), el.getY() - 1, el.getZ()).getBlock();
		if (b.getType() == Material.GRAVEL) {
			this.makeSound("step.gravel", 0.15F, 1.0F);
		}
		else if (b.getType() == Material.LADDER) {
			this.makeSound("step.ladder", 0.15F, 1.0F);
		}
		else if (b.getType() == Material.SAND) {
			this.makeSound("step.sand", 0.15F, 1.0F);
		}
		else if (b.getType() == Material.SNOW) {
			this.makeSound("step.snow", 0.15F, 1.0F);
		}
		else if (b.getType() == Material.STONE) {
			this.makeSound("step.stone", 0.15F, 1.0F);
		}
		else if (b.getType() == Material.WOOD) {
			this.makeSound("step.wood", 0.15F, 1.0F);
		}
		if (b.getType() == Material.WOOL) {
			this.makeSound("step.cloth", 0.15F, 1.0F);
		}
		else {
			this.makeSound("step.grass", 0.15F, 1.0F);
		}
	}

	@Override
	protected String r() {
		return "random.breath";
	}
}*/