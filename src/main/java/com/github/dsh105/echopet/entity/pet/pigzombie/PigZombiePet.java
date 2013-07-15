package com.github.dsh105.echopet.entity.pet.pigzombie;

import org.bukkit.entity.Player;

import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.entity.pet.Pet;


public class PigZombiePet extends Pet {

	boolean baby = false;
	boolean villager = false;
	boolean equipment = false;
	
	public PigZombiePet(Player owner, PetType petType) {
		super(owner, petType);
		//this.equipment = EchoPet.getPluginInstance().DO.shouldHaveEquipment(getPet().getPetType());
	}
	
	public void setBaby(boolean flag) {
		((EntityPigZombiePet) getEntityPet()).setBaby(flag);
		this.baby = flag;
	}
	
	public boolean isBaby() {
		return this.baby;
	}
	
	public void setVillager(boolean flag) {
		((EntityPigZombiePet) getEntityPet()).setVillager(flag);
		this.villager = flag;
	}
	
	public boolean isVillager() {
		return this.villager;
	}

}
