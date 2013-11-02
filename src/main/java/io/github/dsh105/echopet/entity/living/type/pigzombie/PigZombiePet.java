package io.github.dsh105.echopet.entity.living.type.pigzombie;

import io.github.dsh105.echopet.entity.living.data.PetType;
import io.github.dsh105.echopet.entity.living.LivingPet;
import org.bukkit.entity.Player;


public class PigZombiePet extends LivingPet {

    boolean baby = false;
    boolean villager = false;
    boolean equipment = false;

    public PigZombiePet(Player owner, PetType petType) {
        super(owner, petType);
        //this.equipment = EchoPet.getPluginInstance().options.shouldHaveEquipment(getPet().getPetType());
    }

    public void setBaby(boolean flag) {
        ((EntityPigZombiePet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    public boolean isBaby() {
        return this.baby;
    }

    public void setVillager(boolean flag) {
        ((EntityPigZombiePet) getEntityPet()).setVillager(flag);
        this.villager = flag;
    }

    public boolean isVillager() {
        return this.villager;
    }

}
