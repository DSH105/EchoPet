package io.github.dsh105.echopet.entity.type.irongolem;

import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;

@EntityPetType(petType = PetType.IRONGOLEM)
public class IronGolemPet extends Pet {

    public IronGolemPet(String owner) {
        super(owner);
    }

}
