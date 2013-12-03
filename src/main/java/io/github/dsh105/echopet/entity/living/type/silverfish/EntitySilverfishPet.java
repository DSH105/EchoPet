package io.github.dsh105.echopet.entity.living.type.silverfish;

import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import io.github.dsh105.echopet.entity.living.SizeCategory;
import net.minecraft.server.v1_7_R1.World;

public class EntitySilverfishPet extends EntityLivingPet {

    public EntitySilverfishPet(World world) {
        super(world);
    }

    public EntitySilverfishPet(World world, LivingPet pet) {
        super(world, pet);
        this.a(0.3F, 0.7F);
        this.fireProof = true;
    }

    @Override
    protected void makeStepSound() {
        this.makeSound("mob.silverfish.step", 0.15F, 1.0F);
    }

    @Override
    protected String getIdleSound() {
        return "mob.silverfish.say";
    }

    @Override
    protected String getDeathSound() {
        return "mob.silverfish.kill";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.TINY;
    }
}