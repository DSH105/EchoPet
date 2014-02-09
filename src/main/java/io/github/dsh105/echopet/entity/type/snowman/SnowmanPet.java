package io.github.dsh105.echopet.entity.type.snowman;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;

@EntityPetType(petType = PetType.SNOWMAN)
public class SnowmanPet extends Pet {

    public SnowmanPet(String owner) {
        super(owner);
    }

    public SnowmanPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }
}
