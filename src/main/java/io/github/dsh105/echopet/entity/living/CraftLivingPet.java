package io.github.dsh105.echopet.entity.living;

import io.github.dsh105.echopet.entity.CraftPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;

public class CraftLivingPet extends CraftPet {

    public CraftLivingPet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }

    @Override
    public EntityLivingPet getHandle() {
        if (this.entityPet instanceof EntityLivingPet) {
            return (EntityLivingPet) this.entityPet;
        }
        return null;
    }

    @Override
    public LivingPet getPet() {
        if (this.entityPet.getPet() instanceof LivingPet) {
            return (LivingPet) entityPet.getPet();
        }
        return null;
    }
}
