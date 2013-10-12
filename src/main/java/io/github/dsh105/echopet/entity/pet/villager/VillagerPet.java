package com.github.dsh105.echopet.entity.pet.villager;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager.Profession;

import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.entity.pet.IAgeablePet;
import com.github.dsh105.echopet.entity.pet.Pet;


public class VillagerPet extends Pet implements IAgeablePet {
	
	boolean baby = false;
	Profession profession = Profession.FARMER;
	
	public VillagerPet(Player owner, PetType petType) {
		super(owner, petType);
	}
	
	public boolean isBaby() {
		return this.baby;
	}
	
	public void setBaby(boolean flag) {
		((EntityVillagerPet) getEntityPet()).setBaby(flag);
		this.baby = flag;
	}
	
	public Profession getProfession() {
		return profession;
	}
	
	public int getProfessionId() {
		return profession.getId();
	}
	
	public void setProfession(Profession prof) {
		((EntityVillagerPet) getEntityPet()).setProfession(prof.getId());
		this.profession = prof;
	}
	
}
