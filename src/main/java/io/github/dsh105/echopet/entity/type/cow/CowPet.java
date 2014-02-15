package io.github.dsh105.echopet.entity.type.cow;

import io.github.dsh105.echopet.entity.*;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.COW)
public class CowPet extends Pet implements IAgeablePet {

    boolean baby;

    public CowPet(Player owner) {
        super(owner);
    }

    public CowPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }

    public void setBaby(boolean flag) {
        ((EntityCowPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    public boolean isBaby() {
        return this.baby;
    }

}
