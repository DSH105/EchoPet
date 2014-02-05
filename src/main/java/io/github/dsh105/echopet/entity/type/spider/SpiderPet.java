package io.github.dsh105.echopet.entity.type.spider;


import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;

@EntityPetType(petType = PetType.SPIDER)
public class SpiderPet extends Pet {

    public SpiderPet(String owner) {
        super(owner);
    }

}