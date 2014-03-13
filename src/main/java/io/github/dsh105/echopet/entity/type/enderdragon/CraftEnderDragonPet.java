package io.github.dsh105.echopet.entity.type.enderdragon;

import io.github.dsh105.echopet.entity.CraftPet;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.EnderDragon;

import java.util.Set;

@EntityPetType(petType = PetType.ENDERDRAGON)
public class CraftEnderDragonPet extends CraftPet implements EnderDragon {

    public CraftEnderDragonPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }

    @Override
    public Set<ComplexEntityPart> getParts() {
        return null;
    }
}