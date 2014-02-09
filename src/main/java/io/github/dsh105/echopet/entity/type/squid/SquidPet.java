package io.github.dsh105.echopet.entity.type.squid;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;

@EntityPetType(petType = PetType.SQUID)
public class SquidPet extends Pet {

    public SquidPet(String owner) {
        super(owner);
    }

    public SquidPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }
}
