package io.github.dsh105.echopet.entity.living.type.zombie;

import io.github.dsh105.echopet.entity.living.LivingPet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.entity.Player;


public class ZombiePet extends LivingPet {

    boolean baby = false;
    boolean villager = false;
    boolean equipment = false;

    public ZombiePet(Player owner, PetType petType) {
        super(owner, petType);
        //this.equipment = EchoPet.getInstance().options.shouldHaveEquipment(getPet().getPetType());
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
