package io.github.dsh105.echopet.entity.type.pigzombie;

import io.github.dsh105.echopet.entity.*;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.PigZombie;

@EntityPetType(petType = PetType.PIGZOMBIE)
public class CraftPigZombiePet extends CraftPet implements PigZombie {

    public CraftPigZombiePet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }

    @Override
    public int getAnger() {
        return 0; // Doesn't apply to Pets
    }

    @Override
    public void setAnger(int i) {
        // Doesn't apply to Pets
    }

    @Override
    public void setAngry(boolean b) {
        // Doesn't apply to Pets
    }

    @Override
    public boolean isAngry() {
        return false; // Doesn't apply to Pets
    }

    @Override
    public boolean isBaby() {
        Pet p = this.getPet();
        if (p instanceof PigZombiePet) {
            return ((PigZombiePet) p).isBaby();
        }
        return false;
    }

    @Override
    public void setBaby(boolean b) {
        /*Pet p = this.getPet();
        if (p instanceof PigZombiePet) {
            ((PigZombiePet) p).setBaby(b);
        }*/
    }

    @Override
    public boolean isVillager() {
        Pet p = this.getPet();
        if (p instanceof PigZombiePet) {
            return ((PigZombiePet) p).isBaby();
        }
        return false;
    }

    @Override
    public void setVillager(boolean b) {
        /*Pet p = this.getPet();
        if (p instanceof PigZombiePet) {
            ((PigZombiePet) p).setVillager(b);
        }*/
    }
}