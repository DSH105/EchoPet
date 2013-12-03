package io.github.dsh105.echopet.entity.living.type.slime;

import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import io.github.dsh105.echopet.entity.living.SizeCategory;
import io.github.dsh105.echopet.logger.Logger;
import io.github.dsh105.echopet.util.Particle;
import net.minecraft.server.v1_7_R1.World;

public class EntitySlimePet extends EntityLivingPet {

    public EntitySlimePet(World world) {
        super(world);
    }

    int jumpDelay;

    public EntitySlimePet(World world, LivingPet pet) {
        super(world, pet);
        this.fireProof = true;
        int i = 1 << this.random.nextInt(3);
        this.setSize(i);
        this.jumpDelay = this.random.nextInt(15) + 10;
    }

    public void setSize(int i) {
        this.datawatcher.watch(16, new Byte((byte) i));
        this.a(0.6F * (float) i, 0.6F * (float) i);
        this.setPosition(this.locX, this.locY, this.locZ);
        this.setHealth(this.getMaxHealth());
        ((SlimePet) pet).size = i;
    }

    public int getSize() {
        return this.datawatcher.getByte(16);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, new Byte((byte) 1));
    }

    @Override
    protected String getIdleSound() {
        return "";
    }

    @Override
    protected String getDeathSound() {
        return "mob.slime." + (this.getSize() > 1 ? "big" : "small");
    }

    @Override
    public void onLive() {
        super.onLive();

        if (this.onGround && this.jumpDelay-- <= 0) {
            this.jumpDelay = this.random.nextInt(15) + 10;
            this.makeSound(this.getDeathSound(), this.bf(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            getControllerJump().a();
        }

        if (this.random.nextBoolean() && particle <= 0 && !this.isInvisible()) {
            try {
                Particle.SLIME_SPLAT.sendTo(pet.getLocation());
            } catch (Exception e) {
                Logger.log(Logger.LogLevel.WARNING, "Particle effect creation failed.", e, true);
            }
        }
    }

    @Override
    public SizeCategory getSizeCategory() {
        if (this.getSize() == 1) {
            return SizeCategory.TINY;
        } else if (this.getSize() == 4) {
            return SizeCategory.LARGE;
        } else {
            return SizeCategory.REGULAR;
        }
    }
}