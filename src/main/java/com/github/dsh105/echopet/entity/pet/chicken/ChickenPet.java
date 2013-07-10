package com.github.dsh105.echopet.entity.pet.chicken;


import org.bukkit.entity.Player;

import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.entity.pet.IAgeablePet;
import com.github.dsh105.echopet.entity.pet.Pet;

public class ChickenPet extends Pet implements IAgeablePet {
	
	boolean baby;
	
	public ChickenPet(Player owner, PetType petType) {
		super(owner, petType);
	}

	public void setBaby(boolean flag) {
		((EntityChickenPet) getPet()).setBaby(flag);
		this.baby = flag;
	}
	
	public boolean isBaby() {
		return this.baby;
	}
}