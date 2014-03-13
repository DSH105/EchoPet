package io.github.dsh105.echopet.entity.type.zombie;

import io.github.dsh105.echopet.entity.*;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Zombie;

@EntityPetType(petType = PetType.ZOMBIE)
public class CraftZombiePet extends CraftPet implements Zombie {

    public CraftZombiePet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }

    @Override
    public boolean isBaby() {
        Pet p = this.getPet();
        if (p instanceof ZombiePet) {
            return ((ZombiePet) p).isBaby();
        }
        return false;
    }

    @Override
    public void setBaby(boolean b) {
        /*Pet p = this.getPet();
        if (p instanceof ZombiePet) {
            ((ZombiePet) p).setBaby(b);
        }*/
    }

    @Override
    public boolean isVillager() {
        Pet p = this.getPet();
        if (p instanceof ZombiePet) {
            return ((ZombiePet) p).isVillager();
        }
        return false;
    }

    @Override
    public void setVillager(boolean b) {
        /*Pet p = this.getPet();
        if (p instanceof ZombiePet) {
            ((ZombiePet) p).setVillager(b);
        }*/
    }
}