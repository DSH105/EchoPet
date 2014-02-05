package io.github.dsh105.echopet.entity.type.silverfish;

import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;

@EntityPetType(petType = PetType.SILVERFISH)
public class SilverfishPet extends Pet {

    public SilverfishPet(String owner) {
        super(owner);
    }

}
