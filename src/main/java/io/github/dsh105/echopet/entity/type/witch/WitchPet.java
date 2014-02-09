package io.github.dsh105.echopet.entity.type.witch;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;

@EntityPetType(petType = PetType.WITCH)
public class WitchPet extends Pet {

    public WitchPet(String owner) {
        super(owner);
    }

    public WitchPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }
}
