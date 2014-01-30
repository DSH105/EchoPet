package io.github.dsh105.echopet.entity.living.type.ghast;

import io.github.dsh105.echopet.entity.SizeCategory;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import net.minecraft.server.v1_7_R1.World;

public class EntityGhastPet extends EntityLivingPet {

    public EntityGhastPet(World world) {
        super(world);
    }

    public EntityGhastPet(World world, LivingPet pet) {
        super(world, pet);
        this.a(4.0F, 4.0F);
        this.fireProof = true;
    }

    @Override
    protected String getIdleSound() {
        return "mob.ghast.moan";
    }

    @Override
    protected String getDeathSound() {
        return "mob.ghast.death";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.OVERSIZE;
    }
}