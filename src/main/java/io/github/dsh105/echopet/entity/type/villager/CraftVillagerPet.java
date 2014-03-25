package io.github.dsh105.echopet.entity.type.villager;

import io.github.dsh105.echopet.entity.CraftAgeablePet;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.craftbukkit.v1_7_R2.CraftServer;
import org.bukkit.entity.Villager;

@EntityPetType(petType = PetType.VILLAGER)
public class CraftVillagerPet extends CraftAgeablePet implements Villager {

    public CraftVillagerPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }

    @Override
    public Profession getProfession() {
        Pet p = this.getPet();
        if (p instanceof VillagerPet) {
            return ((VillagerPet) p).getProfession();
        }
        return null;
    }

    @Override
    public void setProfession(Profession profession) {
        /*Pet p = this.getPet();
        if (p instanceof VillagerPet) {
            ((VillagerPet) p).setProfession(profession);
        }*/
    }
}