package io.github.dsh105.echopet.entity.pet.blaze;

import org.bukkit.entity.Player;

import io.github.dsh105.echopet.data.PetType;
import io.github.dsh105.echopet.entity.pet.Pet;


public class BlazePet extends Pet {
	
	boolean onFire;

	public BlazePet(Player owner, PetType petType) {
		super(owner, petType);
	}

	public void setOnFire(boolean flag) {
		((EntityBlazePet) getEntityPet()).setOnFire(flag);
		this.onFire = flag;
	}
	
	public boolean isOnFire() {
		return this.onFire;
	}
}
