package io.github.dsh105.echopet.entity.living.type.wither;

import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.entity.living.LivingPet;
import org.bukkit.entity.Player;


public class WitherPet extends LivingPet {

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
