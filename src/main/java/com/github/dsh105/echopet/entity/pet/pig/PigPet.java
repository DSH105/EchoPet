package com.github.dsh105.echopet.entity.pet.pig;


import org.bukkit.entity.Player;

import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.entity.pet.IAgeablePet;
import com.github.dsh105.echopet.entity.pet.Pet;

public class PigPet extends Pet implements IAgeablePet {
	
	boolean baby;
	boolean saddle;

	public PigPet(Player owner, PetType petType) {
		super(owner, petType);
	}
	
	public void setBaby(boolean flag) {
		((EntityPigPet) getPet()).setBaby(flag);
		this.baby = flag;
	}
	
	public boolean isBaby() {
		return this.baby;
	}
	
	public void setSaddle(boolean flag) {
		((EntityPigPet) getPet()).setSaddle(flag);
		this.saddle = flag;
	}
	
	public boolean hasSaddle() {
		return this.saddle;
	}
}