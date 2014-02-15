package io.github.dsh105.echopet.entity.type.zombie;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.ZOMBIE)
public class ZombiePet extends Pet {

    boolean baby = false;
    boolean villager = false;

    public ZombiePet(Player owner) {
        super(owner);
    }

    public ZombiePet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }

    public void setBaby(boolean flag) {
        ((EntityZombiePet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    public boolean isBaby() {
        return this.baby;
    }

    public void setVillager(boolean flag) {
        ((EntityZombiePet) getEntityPet()).setVillager(flag);
        this.villager = flag;
    }

    public boolean isVillager() {
        return this.villager;
    }

}
