package io.github.dsh105.echopet.entity.living.type.mushroomcow;

import io.github.dsh105.echopet.entity.living.LivingPet;
import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.entity.living.IAgeablePet;
import org.bukkit.entity.Player;


public class MushroomCowPet extends LivingPet implements IAgeablePet {

    boolean baby;

    public MushroomCowPet(Player owner, PetType petType) {
        super(owner, petType);
    }

    public void setBaby(boolean flag) {
        ((EntityMushroomCowPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    public boolean isBaby() {
        return this.baby;
    }

}
