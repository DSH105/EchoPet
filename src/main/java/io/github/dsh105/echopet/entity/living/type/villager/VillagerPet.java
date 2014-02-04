package io.github.dsh105.echopet.entity.living.type.villager;

import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.entity.living.IAgeablePet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager.Profession;


public class VillagerPet extends LivingPet implements IAgeablePet {

    boolean baby = false;
    Profession profession = Profession.FARMER;

    public VillagerPet(Player owner, PetType petType) {
        super(owner, petType);
    }

    @Override
    public boolean isBaby() {
        return this.baby;
    }

    @Override
    public void setBaby(boolean flag) {
        ((EntityVillagerPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    public Profession getProfession() {
        return profession;
    }

    public int getProfessionId() {
        return profession.getId();
    }

    public void setProfession(Profession prof) {
        ((EntityVillagerPet) getEntityPet()).setProfession(prof.getId());
        this.profession = prof;
    }

}
