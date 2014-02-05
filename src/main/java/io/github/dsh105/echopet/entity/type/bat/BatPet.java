package io.github.dsh105.echopet.entity.type.bat;

import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;

@EntityPetType(petType = PetType.BAT)
public class BatPet extends Pet {

    boolean hanging;

    public BatPet(String owner) {
        super(owner);
    }

    public void setHanging(boolean flag) {
        ((EntityBatPet) getEntityPet()).setHanging(flag);
        this.hanging = flag;
    }

    public boolean isHanging() {
        return this.hanging;
    }
}
