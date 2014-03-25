package io.github.dsh105.echopet.entity.type.pig;

import io.github.dsh105.echopet.entity.CraftAgeablePet;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.craftbukkit.v1_7_R2.CraftServer;
import org.bukkit.entity.Pig;

@EntityPetType(petType = PetType.PIG)
public class CraftPigPet extends CraftAgeablePet implements Pig {

    public CraftPigPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }

    @Override
    public boolean hasSaddle() {
        Pet p = this.getPet();
        if (p instanceof PigPet) {
            return ((PigPet) p).hasSaddle();
        }
        return false;
    }

    @Override
    public void setSaddle(boolean b) {
        /*Pet p = this.getPet();
        if (p instanceof PigPet) {
            ((PigPet) p).setSaddle(b);
        }*/
    }
}