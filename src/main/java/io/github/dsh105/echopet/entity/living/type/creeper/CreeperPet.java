package io.github.dsh105.echopet.entity.living.type.creeper;

import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.entity.living.LivingPet;
import org.bukkit.entity.Player;


public class CreeperPet extends LivingPet {

    boolean powered;
    boolean ignited;

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

    public void setIgnited(boolean flag) {
        ((EntityCreeperPet) getEntityPet()).setIgnited(flag);
        this.ignited = flag;
    }

    public boolean isIgnited() {
        return this.ignited;
    }
}
