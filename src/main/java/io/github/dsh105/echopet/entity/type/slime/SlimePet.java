package io.github.dsh105.echopet.entity.type.slime;

import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;

@EntityPetType(petType = PetType.SLIME)
public class SlimePet extends Pet {

    int size;

    public SlimePet(String owner) {
        super(owner);
    }

    public void setSize(int i) {
        ((EntitySlimePet) getEntityPet()).setSize(i);
        this.size = i;
    }

    public int getSize() {
        return this.size;
    }
}
