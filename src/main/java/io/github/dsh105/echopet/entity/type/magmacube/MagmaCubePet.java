package io.github.dsh105.echopet.entity.type.magmacube;

import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;

@EntityPetType(petType = PetType.MAGMACUBE)
public class MagmaCubePet extends Pet {

    int size;

    public MagmaCubePet(String owner) {
        super(owner);
    }

    public void setSize(int i) {
        ((EntityMagmaCubePet) getEntityPet()).setSize(i);
        this.size = i;
    }

    public int getSize() {
        return this.size;
    }
}
