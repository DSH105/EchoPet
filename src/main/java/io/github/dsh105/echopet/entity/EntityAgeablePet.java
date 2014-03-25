package io.github.dsh105.echopet.entity;

import net.minecraft.server.v1_7_R2.World;

public abstract class EntityAgeablePet extends EntityPet {

    private boolean ageLocked = true;

    public EntityAgeablePet(World world) {
        super(world);
    }

    public EntityAgeablePet(World world, Pet pet) {
        super(world, pet);
    }

    public int getAge() {
        return this.datawatcher.getInt(12);
    }

    public void setAge(int i) {
        this.datawatcher.watch(12, Integer.valueOf(i));
    }

    public boolean isAgeLocked() {
        return ageLocked;
    }

    public void setAgeLocked(boolean ageLocked) {
        this.ageLocked = ageLocked;
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(12, new Integer(0));
    }

    @Override
    public void e() {
        super.e();
        if (!(this.world.isStatic || this.ageLocked)) {
            int i = this.getAge();

            if (i < 0) {
                ++i;
                this.setAge(i);
            } else if (i > 0) {
                --i;
                this.setAge(i);
            }
        }
    }

    public void setBaby(boolean flag) {
        if (flag) {
            this.datawatcher.watch(12, Integer.valueOf(Integer.MIN_VALUE));
        } else {
            this.datawatcher.watch(12, new Integer(0));
        }
    }

    @Override
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