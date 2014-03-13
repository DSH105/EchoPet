package io.github.dsh105.echopet.entity.type.wither;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.WITHER)
public class WitherPet extends Pet {

    boolean shield = false;

    public WitherPet(Player owner) {
        super(owner);
    }

    public WitherPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }

    public void setShielded(boolean flag) {
        ((EntityWitherPet) getEntityPet()).setShielded(flag);
        this.shield = flag;
    }

    public boolean isShielded() {
        return this.shield;
    }

}
