package com.github.dsh105.echopet.data;

import java.util.ArrayList;

public class UnorganisedPetData {
	
	public ArrayList<PetData> petDataList;
	public PetType petType;
	public String petName;
	
	public UnorganisedPetData(ArrayList<PetData> petDataList, PetType petType, String petName) {
		this.petDataList = petDataList;
		this.petType = petType;
		this.petName = petName;
	}
}
