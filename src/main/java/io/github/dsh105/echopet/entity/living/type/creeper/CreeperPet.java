package io.github.dsh105.echopet.entity.living.type.creeper;

import io.github.dsh105.echopet.entity.living.data.PetType;
import io.github.dsh105.echopet.entity.living.LivingPet;
import org.bukkit.entity.Player;


public class CreeperPet extends LivingPet {

    boolean powered;

    public CreeperPet(Player owner, PetType petType) {
        super(owner, petType);
    }

    public void setPowered(boolean flag) {
        ((EntityCreeperPet) getEntityPet()).setPowered(flag);
        this.powered = flag;
    }

    public boolean isPowered() {
        return this.powered;
    }
}
