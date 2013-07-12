package com.github.dsh105.echopet.entity.pet.horse;

import org.bukkit.entity.Player;

import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.entity.pet.IAgeablePet;
import com.github.dsh105.echopet.entity.pet.Pet;


public class HorsePet extends Pet implements IAgeablePet {
	
	HorseType horseType;
	HorseVariant variant;
	HorseMarking marking;
	HorseArmour armour;
	boolean baby = false;
	boolean chested = false;
	boolean saddle = false;
	
	public HorsePet(Player owner, PetType petType) {
		super(owner, petType);
	}
	
	public void setHorseType(HorseType type) {
		((EntityHorsePet) getPet()).setType(type);
		this.horseType = type;
	}
	
	public void setVariant(HorseVariant variant, HorseMarking marking) {
		((EntityHorsePet) getPet()).setVariant(variant, marking);
		this.variant = variant;
		this.marking = marking;
	}
	
	public void setArmour(HorseArmour armour) {
		((EntityHorsePet) getPet()).setArmour(armour);
		this.armour = armour;
	}
	
	public void setBaby(boolean flag) {
		((EntityHorsePet) getPet()).setBaby(flag);
		this.baby = flag;
	}
	
	public void setSaddled(boolean flag) {
		((EntityHorsePet) getPet()).setSaddled(flag);
		this.saddle = flag;
	}
	
	public void setChested(boolean flag) {
		((EntityHorsePet) getPet()).setChested(flag);
		this.chested = flag;
	}
	
	public HorseType getHorseType() {
		return this.horseType;
	}
	
	public HorseVariant getVariant() {
		return this.variant;
	}
	
	public HorseMarking getMarking() {
		return this.marking;
	}
	
	public HorseArmour getArmour() {
		return this.armour;
	}
	
	public boolean isBaby() {
		return this.baby;
	}
	
	public boolean isSaddled() {
		return this.saddle;
	}
	
	public boolean isChested() {
		return this.chested;
	}
}