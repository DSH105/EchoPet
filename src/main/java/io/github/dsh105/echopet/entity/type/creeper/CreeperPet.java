package io.github.dsh105.echopet.entity.type.creeper;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.CREEPER)
public class CreeperPet extends Pet {

    boolean powered;
    boolean ignited;

    public CreeperPet(Player owner) {
        super(owner);
    }

    public CreeperPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
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
