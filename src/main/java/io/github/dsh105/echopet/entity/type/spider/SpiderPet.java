package io.github.dsh105.echopet.entity.type.spider;


import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;

@EntityPetType(petType = PetType.SPIDER)
public class SpiderPet extends Pet {

    public SpiderPet(String owner) {
        super(owner);
    }

    public SpiderPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }
}