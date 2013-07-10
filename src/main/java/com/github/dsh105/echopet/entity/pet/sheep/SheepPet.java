package com.github.dsh105.echopet.entity.pet.sheep;

import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.entity.pet.IAgeablePet;
import com.github.dsh105.echopet.entity.pet.Pet;


public class SheepPet extends Pet implements IAgeablePet {
	
	boolean baby;
	boolean sheared;
	byte color;

	public SheepPet(Player owner, PetType petType) {
		super(owner, petType);
	}
	
	public void setBaby(boolean flag) {
		((EntitySheepPet) getPet()).setBaby(flag);
		this.baby = flag;
	}
	
	public boolean isBaby() {
		return this.baby;
	}
	
	public void setSheared(boolean flag) {
		((EntitySheepPet) getPet()).setSheared(flag);
		this.sheared = flag;
	}
	
	public boolean isSheared() {
		return this.sheared;
	}
	
	public DyeColor getColor() {
		return org.bukkit.DyeColor.getByWoolData(color);
	}
	
	public byte getColorByte() {
		return color;
	}

	public void setColor(DyeColor c) {
		((EntitySheepPet) getPet()).setColor(c.getWoolData());
	}
	
	public void setColor(byte b) {
		((EntitySheepPet) getPet()).setColor(b);
	}
	
}
