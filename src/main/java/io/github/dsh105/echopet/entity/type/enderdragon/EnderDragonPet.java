package io.github.dsh105.echopet.entity.type.enderdragon;

import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;

@EntityPetType(petType = PetType.ENDERDRAGON)
public class EnderDragonPet extends Pet {

    public EnderDragonPet(String owner) {
        super(owner);
    }
}