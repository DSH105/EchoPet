package com.github.dsh105.echopet.entity.pathfinder.goals;

import net.minecraft.server.v1_6_R2.EntityPlayer;

import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;

import com.github.dsh105.echopet.entity.pathfinder.PetGoal;
import com.github.dsh105.echopet.entity.pet.EntityPet;
import com.github.dsh105.echopet.util.ReflectionUtil;

public class PetGoalRandomStroll extends PetGoal {
	
	private EntityPet a;
	private double b;
	private double c;
	private double d;
	private float e = 0.4F;
	private EntityPlayer f;
	
	public PetGoalRandomStroll(EntityPet pet) {
		this.a = pet;
		this.f = ((CraftPlayer) a.getOwner()).getHandle();
	}
	
	@Override
	public boolean a() {
		if (this.a.aE() >= 100) {
			return false;
		}
		else if (ReflectionUtil.isInBorder(a.getOwner().getLocation(), a.getLocation(), 3)) {
			this.b = f.locX;
			this.c = f.locY;
			this.d = f.locZ;
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public void e() {
		this.a.getNavigation().a(b, c, d, e);
	}

}