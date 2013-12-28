package io.github.dsh105.echopet.data;

import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.entity.living.PetData;

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
