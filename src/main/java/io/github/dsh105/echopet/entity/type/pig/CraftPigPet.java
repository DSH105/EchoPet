package io.github.dsh105.echopet.entity.type.pig;

import io.github.dsh105.echopet.entity.*;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
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