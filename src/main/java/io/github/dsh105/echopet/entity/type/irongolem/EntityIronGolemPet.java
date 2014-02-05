package io.github.dsh105.echopet.entity.type.irongolem;

import io.github.dsh105.echopet.entity.*;
import net.minecraft.server.v1_7_R1.Entity;
import net.minecraft.server.v1_7_R1.World;

@EntitySize(width = 1.4F, height = 1.9F)
public class EntityIronGolemPet extends EntityPet {

    public EntityIronGolemPet(World world) {
        super(world);
    }

    public EntityIronGolemPet(World world, Pet pet) {
        super(world, pet);
    }

    @Override
    public PetType getEntityPetType() {
        return PetType.IRONGOLEM;
    }

    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
    }

    @Override
    public boolean attack(Entity entity) {
        boolean flag = super.attack(entity);
        if (flag) {
            this.world.broadcastEntityEffect(this, (byte) 4);
            entity.motY = 0.4000000059604645D;
            this.makeSound("mob.irongolem.throw", 1.0F, 1.0F);
        }
        return flag;
    }

    @Override
    protected void makeStepSound() {
        this.makeSound("mob.irongolem.walk", 1.0F, 1.0F);
    }

    @Override
    protected String getIdleSound() {
        return "none";
    }

    @Override
    protected String getDeathSound() {
        return "mob.irongolem.death";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.LARGE;
    }
}