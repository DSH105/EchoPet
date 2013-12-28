package io.github.dsh105.echopet.entity.living.type.cow;

import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.entity.living.IAgeablePet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import org.bukkit.entity.Player;


public class CowPet extends LivingPet implements IAgeablePet {

    boolean baby;

    public CowPet(Player owner, PetType petType) {
        super(owner, petType);
    }

    public void setBaby(boolean flag) {
        ((EntityCowPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    public boolean isBaby() {
        return this.baby;
    }

}
