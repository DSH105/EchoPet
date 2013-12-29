package io.github.dsh105.echopet.entity.living.type.skeleton;

import io.github.dsh105.echopet.entity.living.CraftLivingPet;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Skeleton;

public class CraftSkeletonPet extends CraftLivingPet implements Skeleton {

    public CraftSkeletonPet(CraftServer server, EntityLivingPet entity) {
        super(server, entity);
    }

    @Override
    public SkeletonType getSkeletonType() {
        return null;
    }

    @Override
    public void setSkeletonType(SkeletonType skeletonType) {

    }
}