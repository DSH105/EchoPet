package io.github.dsh105.echopet.entity.type.mushroomcow;

import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.IAgeablePet;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;

@EntityPetType(petType = PetType.MUSHROOMCOW)
public class MushroomCowPet extends Pet implements IAgeablePet {

    boolean baby;

    public MushroomCowPet(String owner) {
        super(owner);
    }

    public void setBaby(boolean flag) {
        ((EntityMushroomCowPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    public boolean isBaby() {
        return this.baby;
    }

}
