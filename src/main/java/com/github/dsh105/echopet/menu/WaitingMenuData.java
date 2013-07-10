package com.github.dsh105.echopet.menu;

import java.util.ArrayList;
import java.util.HashMap;

import com.github.dsh105.echopet.data.PetData;
import com.github.dsh105.echopet.entity.pet.Pet;


public class WaitingMenuData {
	
	public static HashMap<Pet, WaitingMenuData> waiting = new HashMap<Pet, WaitingMenuData>();
	
	public Pet pet;
	public ArrayList<PetData> petDataTrue;
	public ArrayList<PetData> petDataFalse;
	
	public WaitingMenuData(Pet pet) {
		this.pet = pet;
		this.petDataTrue = new ArrayList<PetData>();
		this.petDataFalse = new ArrayList<PetData>();
		waiting.put(this.pet, this);
	}
}
