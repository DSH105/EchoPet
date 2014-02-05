package io.github.dsh105.echopet.entity.type.horse;

import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.IAgeablePet;
import io.github.dsh105.echopet.entity.PetType;

@EntityPetType(petType = PetType.HORSE)
public class HorsePet extends Pet implements IAgeablePet {

    HorseType horseType;
    HorseVariant variant;
    HorseMarking marking;
    HorseArmour armour;
    boolean baby = false;
    boolean chested = false;
    boolean saddle = false;

    public HorsePet(String owner) {
        super(owner);
    }

    public void setHorseType(HorseType type) {
        ((EntityHorsePet) getEntityPet()).setType(type);
        this.horseType = type;
    }

    public void setVariant(HorseVariant variant, HorseMarking marking) {
        ((EntityHorsePet) getEntityPet()).setVariant(variant, marking);
        this.variant = variant;
        this.marking = marking;
    }

    public void setArmour(HorseArmour armour) {
        ((EntityHorsePet) getEntityPet()).setArmour(armour);
        this.armour = armour;
    }

    public void setBaby(boolean flag) {
        ((EntityHorsePet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    public void setSaddled(boolean flag) {
        ((EntityHorsePet) getEntityPet()).setSaddled(flag);
        this.saddle = flag;
    }

    public void setChested(boolean flag) {
        ((EntityHorsePet) getEntityPet()).setChested(flag);
        this.chested = flag;
    }

    public HorseType getHorseType() {
        return this.horseType;
    }

    public HorseVariant getVariant() {
        return this.variant;
    }

    public HorseMarking getMarking() {
        return this.marking;
    }

    public HorseArmour getArmour() {
        return this.armour;
    }

    public boolean isBaby() {
        return this.baby;
    }

    public boolean isSaddled() {
        return this.saddle;
    }

    public boolean isChested() {
        return this.chested;
    }
}