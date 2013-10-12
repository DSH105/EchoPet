package io.github.dsh105.echopet.entity.pet.wither;

import io.github.dsh105.echopet.data.PetType;
import io.github.dsh105.echopet.entity.pet.Pet;
import org.bukkit.entity.Player;


public class WitherPet extends Pet {

    boolean shield = false;

    public WitherPet(Player owner, PetType petType) {
        super(owner, petType);
    }

    public void setShielded(boolean flag) {
        ((EntityWitherPet) getEntityPet()).setShielded(flag);
        this.shield = flag;
    }

    public boolean isShielded() {
        return this.shield;
    }

}
