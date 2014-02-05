package io.github.dsh105.echopet.entity.type.villager;

import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.IAgeablePet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.entity.Villager.Profession;

@EntityPetType(petType = PetType.VILLAGER)
public class VillagerPet extends Pet implements IAgeablePet {

    boolean baby = false;
    Profession profession = Profession.FARMER;

    public VillagerPet(String owner) {
        super(owner);
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
