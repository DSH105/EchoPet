package io.github.dsh105.echopet.entity.type.chicken;


import io.github.dsh105.echopet.entity.*;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.CHICKEN)
public class ChickenPet extends Pet implements IAgeablePet {

    boolean baby;

    public ChickenPet(Player owner) {
        super(owner);
    }

    public ChickenPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }

    public void setBaby(boolean flag) {
        ((EntityChickenPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    public boolean isBaby() {
        return this.baby;
    }
}