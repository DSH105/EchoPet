package io.github.dsh105.echopet.entity.living.type.enderman;

import io.github.dsh105.echopet.entity.living.data.PetType;
import io.github.dsh105.echopet.entity.living.LivingPet;
import org.bukkit.entity.Player;


public class EndermanPet extends LivingPet {

    boolean scream;

    public EndermanPet(Player owner, PetType petType) {
        super(owner, petType);
    }

    public void setScreaming(boolean flag) {
        ((EntityEndermanPet) getEntityPet()).setScreaming(flag);
        this.scream = flag;
    }

    public boolean isScreaming() {
        return this.scream;
    }
}
