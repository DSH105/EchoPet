package io.github.dsh105.echopet.entity.type.cavespider;


import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;

@EntityPetType(petType = PetType.CAVESPIDER)
public class CaveSpiderPet extends Pet {

    public CaveSpiderPet(String owner) {
        super(owner);
    }

}