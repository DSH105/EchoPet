package io.github.dsh105.echopet.entity.type.skeleton;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.SKELETON)
public class SkeletonPet extends Pet {

    boolean wither;

    public SkeletonPet(Player owner) {
        super(owner);
    }

    public SkeletonPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }

    public void setWither(boolean flag) {
        ((EntitySkeletonPet) getEntityPet()).setWither(flag);
        this.wither = flag;
    }

    public boolean isWither() {
        return this.wither;
    }

}