package io.github.dsh105.echopet.entity.living.type.enderman;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.living.CraftLivingPet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.craftbukkit.v1_7_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Enderman;
import org.bukkit.material.MaterialData;

public class CraftEndermanPet extends CraftLivingPet implements Enderman {

    public CraftEndermanPet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }

    @Override
    public MaterialData getCarriedMaterial() {
        EntityPet e = getHandle();
        if (e instanceof EntityEndermanPet) {
            EntityEndermanPet endermanPet = (EntityEndermanPet) e;
            return CraftMagicNumbers.getMaterial(endermanPet.getCarried()).getNewData((byte) endermanPet.getCarriedData());
        }
        return null;
    }

    @Override
    public void setCarriedMaterial(MaterialData data) {
        EntityPet e = getHandle();
        if (e instanceof EntityEndermanPet) {
            EntityEndermanPet endermanPet = (EntityEndermanPet) e;
            endermanPet.setCarried(CraftMagicNumbers.getBlock(data.getItemTypeId()));
            endermanPet.setCarriedData(data.getData());
        }
    }
}