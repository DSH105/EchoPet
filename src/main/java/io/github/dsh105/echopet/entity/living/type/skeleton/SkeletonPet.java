package io.github.dsh105.echopet.entity.living.type.skeleton;

import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.entity.living.LivingPet;
import org.bukkit.entity.Player;


public class SkeletonPet extends LivingPet {

    boolean wither;
    boolean equipment;

    public SkeletonPet(Player owner, PetType petType) {
        super(owner, petType);
        //this.equipment = EchoPet.getInstance().options.shouldHaveEquipment(pet.getPet().getPetType());
    }

    public void setWither(boolean flag) {
        ((EntitySkeletonPet) getEntityPet()).setWither(flag);
        this.wither = flag;
    }

    public boolean isWither() {
        return this.wither;
    }

}