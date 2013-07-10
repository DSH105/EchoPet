package com.github.dsh105.echopet.entity.pet.ocelot;


import org.bukkit.entity.Ocelot.Type;
import org.bukkit.entity.Player;

import com.github.dsh105.echopet.data.PetType;
import com.github.dsh105.echopet.entity.pet.IAgeablePet;
import com.github.dsh105.echopet.entity.pet.Pet;

public class OcelotPet extends Pet implements IAgeablePet {
	
	boolean baby;
	Type type = Type.WILD_OCELOT;

	public OcelotPet(Player owner, PetType petType) {
		super(owner, petType);
	}
	
	public void setBaby(boolean flag) {
		((EntityOcelotPet) getPet()).setBaby(flag);
		this.baby = flag;
	}
	
	public boolean isBaby() {
		return this.baby;
	}
	
	public Type getCatType() {
		return type;
	}

	public void setCatType(Type t) {
		setCatType(t.getId());
	}
	
	public void setCatType(int i) {
		((EntityOcelotPet) getPet()).setCatType(i);
	}
}