package com.github.dsh105.echopet.entity.pet.zombie;

import org.bukkit.entity.Player;

import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.entity.pet.Pet;


public class ZombiePet extends Pet {
	
	boolean baby = false;
	boolean villager = false;
	boolean equipment = false;
	
	public ZombiePet(Player owner, PetType petType) {
		super(owner, petType);
		//this.equipment = EchoPet.getPluginInstance().options.shouldHaveEquipment(getPet().getPetType());
	}
	
	public void setBaby(boolean flag) {
		((EntityZombiePet) getEntityPet()).setBaby(flag);
		this.baby = flag;
	}
	
	public boolean isBaby() {
		return this.baby;
	}
	
	public void setVillager(boolean flag) {
		((EntityZombiePet) getEntityPet()).setVillager(flag);
		this.villager = flag;
	}
	
	public boolean isVillager() {
		return this.villager;
	}

}
