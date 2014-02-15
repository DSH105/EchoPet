package io.github.dsh105.echopet.entity.type.blaze;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.BLAZE)
public class BlazePet extends Pet {

    boolean onFire;

    public BlazePet(Player owner) {
        super(owner);
    }

    public BlazePet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }

    public void setOnFire(boolean flag) {
        ((EntityBlazePet) getEntityPet()).setOnFire(flag);
        this.onFire = flag;
    }

    public boolean isOnFire() {
        return this.onFire;
    }
}
