package io.github.dsh105.echopet.entity.living;

import io.github.dsh105.echopet.entity.SizeCategory;
import net.minecraft.server.v1_7_R1.World;

public abstract class EntityAgeablePet extends EntityLivingPet {

    private boolean ageLocked = true;

    public EntityAgeablePet(World world) {
        super(world);
    }

    public EntityAgeablePet(World world, LivingPet pet) {
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

    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(12, new Integer(0));
    }

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