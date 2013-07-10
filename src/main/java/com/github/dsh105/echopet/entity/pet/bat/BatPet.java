package com.github.dsh105.echopet.entity.pet.bat;

import org.bukkit.entity.Player;

import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.entity.pet.Pet;


public class BatPet extends Pet {

	boolean hanging;
	
	public BatPet(Player owner, PetType petType) {
		super(owner, petType);
	}
	
	public void setHanging(boolean flag) {
		((EntityBatPet) getPet()).setHanging(flag);
		this.hanging = flag;
	}
	
	public boolean isHanging() {
		return this.hanging;
	}
}
