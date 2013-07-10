package com.github.dsh105.echopet.entity.pet.wither;

import org.bukkit.entity.Player;

import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.entity.pet.Pet;


public class WitherPet extends Pet {
	
	boolean shield = false;
	
	public WitherPet(Player owner, PetType petType) {
		super(owner, petType);
	}
	
	public void setShielded(boolean flag) {
		((EntityWitherPet) getPet()).setShielded(flag);
		this.shield = flag;
	}
	
	public boolean isShielded() {
		return this.shield;
	}

}
