package io.github.dsh105.echopet.entity.type.skeleton;

import io.github.dsh105.echopet.entity.*;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.entity.Skeleton;

@EntityPetType(petType = PetType.SKELETON)
public class CraftSkeletonPet extends CraftPet implements Skeleton {

    public CraftSkeletonPet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }

    @Override
    public SkeletonType getSkeletonType() {
        Pet p = this.getPet();
        if (p instanceof SkeletonPet) {
            return ((SkeletonPet) p).isWither() ? SkeletonType.WITHER : SkeletonType.NORMAL;
        }
        return null;
    }

    @Override
    public void setSkeletonType(SkeletonType skeletonType) {
        /*Pet p = this.getPet();
        if (p instanceof SkeletonPet) {
            ((SkeletonPet) p).setWither(skeletonType == SkeletonType.WITHER);
        }*/
    }
}