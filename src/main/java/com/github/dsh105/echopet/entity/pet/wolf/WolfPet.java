package com.github.dsh105.echopet.entity.pet.wolf;

import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.entity.pet.IAgeablePet;
import com.github.dsh105.echopet.entity.pet.Pet;


public class WolfPet extends Pet implements IAgeablePet {
	
	DyeColor collar = DyeColor.RED;
	boolean baby = false;
	boolean tamed = false;
	boolean angry = false;
	
	public WolfPet(Player owner, PetType petType) {
		super(owner, petType);
	}
	
	public void setCollarColor(DyeColor dc) {
		((EntityWolfPet) getPet()).setCollarColor(dc);
		this.collar = dc;
	}
	
	public DyeColor getCollarColor() {
		return this.collar;
	}
	
	public void setBaby(boolean flag) {
		((EntityWolfPet) getPet()).setBaby(flag);
		this.baby = flag;
	}
	
	public boolean isBaby() {
		return this.baby;
	}
	
	public void setTamed(boolean flag) {
		((EntityWolfPet) getPet()).setTamed(flag);
		this.tamed = flag;
	}
	
	public boolean isTamed() {
		return this.tamed;
	}
	
	public void setAngry(boolean flag) {
		((EntityWolfPet) getPet()).setAngry(flag);
		this.angry = flag;
	}
	
	public boolean isAngry() {
		return this.angry;
	}
}