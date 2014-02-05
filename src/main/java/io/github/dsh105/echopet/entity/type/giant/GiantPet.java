package io.github.dsh105.echopet.entity.type.giant;

import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;

@EntityPetType(petType = PetType.GIANT)
public class GiantPet extends Pet {

    public GiantPet(String owner) {
        super(owner);
    }
}