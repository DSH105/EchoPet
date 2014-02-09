package io.github.dsh105.echopet.entity.type.enderman;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;

@EntityPetType(petType = PetType.ENDERMAN)
public class EndermanPet extends Pet {

    boolean scream;

    public EndermanPet(String owner) {
        super(owner);
    }

    public EndermanPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }

    public void setScreaming(boolean flag) {
        ((EntityEndermanPet) getEntityPet()).setScreaming(flag);
        this.scream = flag;
    }

    public boolean isScreaming() {
        return this.scream;
    }
}
