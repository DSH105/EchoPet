package com.github.dsh105.echopet.entity.pet.creeper;

import org.bukkit.entity.Player;

import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.entity.pet.Pet;


public class CreeperPet extends Pet {

	boolean powered;
	
	public CreeperPet(Player owner, PetType petType) {
		super(owner, petType);
	}
	
	public void setPowered(boolean flag) {
		((EntityCreeperPet) getPet()).setPowered(flag);
		this.powered = flag;
	}
	
	public boolean isPowered() {
		return this.powered;
	}
}
