package io.github.dsh105.echopet.entity.type.bat;

import io.github.dsh105.echopet.entity.*;
import net.minecraft.server.v1_7_R1.MathHelper;
import net.minecraft.server.v1_7_R1.World;

@EntitySize(width = 0.5F, height = 0.9F)
public class EntityBatPet extends EntityPet {

    public EntityBatPet(World world) {
        super(world);
    }

    public EntityBatPet(World world, Pet pet) {
        super(world, pet);
    }

    @Override
    public PetType getEntityPetType() {
        return PetType.BAT;
    }

    public void setHanging(boolean flag) {
        byte b0 = this.datawatcher.getByte(16);
        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 1)));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & -2)));
        }
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, new Byte((byte) 0));
    }

    @Override
    protected String getIdleSound() {
        return this.bN() && this.random.nextInt(4) != 0 ? null : "mob.bat.idle";
    }

    @Override
    public void onLive() {
        super.onLive();
        if (this.bN()) {
            this.motX = this.motY = this.motZ = 0.0D;
            this.locY = (double) MathHelper.floor(this.locY) + 1.0D - (double) this.length;
        } else {
            this.motY *= 0.6000000238418579D;
        }
    }

    public boolean bN() {
        return (this.datawatcher.getByte(16) & 1) != 0;
    }

    @Override
    protected String getDeathSound() {
        return "mob.bat.death";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.TINY;
    }
}