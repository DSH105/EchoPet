package io.github.dsh105.echopet.entity.type.wither;

import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;

@EntityPetType(petType = PetType.WITHER)
public class WitherPet extends Pet {

    boolean shield = false;

    public WitherPet(String owner) {
        super(owner);
    }

    public void setShielded(boolean flag) {
        ((EntityWitherPet) getEntityPet()).setShielded(flag);
        this.shield = flag;
    }

    public boolean isShielded() {
        return this.shield;
    }

}
