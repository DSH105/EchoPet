package io.github.dsh105.echopet.entity.type.pig;


import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.IAgeablePet;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;

@EntityPetType(petType = PetType.PIG)
public class PigPet extends Pet implements IAgeablePet {

    boolean baby;
    boolean saddle;

    public PigPet(String owner) {
        super(owner);
    }

    @Override
    public void setBaby(boolean flag) {
        ((EntityPigPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    @Override
    public boolean isBaby() {
        return this.baby;
    }

    public void setSaddle(boolean flag) {
        ((EntityPigPet) getEntityPet()).setSaddle(flag);
        this.saddle = flag;
    }

    public boolean hasSaddle() {
        return this.saddle;
    }
}