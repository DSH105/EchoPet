package io.github.dsh105.echopet.entity.type.mushroomcow;

import io.github.dsh105.echopet.entity.*;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.MUSHROOMCOW)
public class MushroomCowPet extends Pet implements IAgeablePet {

    boolean baby;

    public MushroomCowPet(Player owner) {
        super(owner);
    }

    public MushroomCowPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }

    public void setBaby(boolean flag) {
        ((EntityMushroomCowPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    public boolean isBaby() {
        return this.baby;
    }

}
