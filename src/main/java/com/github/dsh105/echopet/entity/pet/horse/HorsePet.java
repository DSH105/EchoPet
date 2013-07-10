package com.github.dsh105.echopet.entity.pet.horse;

import org.bukkit.entity.Player;

import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.entity.pet.IAgeablePet;
import com.github.dsh105.echopet.entity.pet.Pet;


public class HorsePet extends Pet implements IAgeablePet {
	
	HorseType horseType;
	HorseVariant variant;
	HorseMarking marking;
	boolean baby = false;
	boolean chested = false;
	
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
	
	public void setBaby(boolean flag) {
		((EntityHorsePet) getPet()).setBaby(flag);
		this.baby = flag;
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
	
	public boolean isBaby() {
		return this.baby;
	}
	
	public boolean isChested() {
		return this.chested;
	}
}