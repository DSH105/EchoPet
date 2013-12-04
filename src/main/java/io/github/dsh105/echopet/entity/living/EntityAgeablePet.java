package io.github.dsh105.echopet.entity.living;

import net.minecraft.server.v1_7_R1.World;

public abstract class EntityAgeablePet extends EntityLivingPet {

    public EntityAgeablePet(World world) {
        super(world);
    }

    public EntityAgeablePet(World world, LivingPet pet) {
        super(world, pet);
    }

    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(12, new Integer(0));
    }

    public void setBaby(boolean flag) {
        if (flag) {
            this.datawatcher.watch(12, Integer.valueOf(Integer.MIN_VALUE));
        } else {
            this.datawatcher.watch(12, new Integer(0));
        }
    }

    public boolean isBaby() {
        return this.datawatcher.getInt(12) < 0;
    }

    @Override
    public SizeCategory getSizeCategory() {
        if (this.isBaby()) {
            return SizeCategory.TINY;
        } else {
            return SizeCategory.REGULAR;
        }
    }
}