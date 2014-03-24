package io.github.dsh105.echopet.entity.type.irongolem;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.craftbukkit.v1_7_R2.CraftServer;
import org.bukkit.entity.IronGolem;

@EntityPetType(petType = PetType.IRONGOLEM)
public class CraftIronGolemPet extends CraftPet implements IronGolem {

    public CraftIronGolemPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }

    @Override
    public boolean isPlayerCreated() {
        return false;
    }

    @Override
    public void setPlayerCreated(boolean b) {
        // Doesn't apply to Pets
    }
}