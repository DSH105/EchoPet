package io.github.dsh105.echopet.entity.living.type.blaze;

import io.github.dsh105.dshutils.Particle;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import io.github.dsh105.echopet.entity.living.SizeCategory;
import io.github.dsh105.dshutils.logger.Logger;
import net.minecraft.server.v1_7_R1.World;

public class EntityBlazePet extends EntityLivingPet {

    public EntityBlazePet(World world) {
        super(world);
    }

    public EntityBlazePet(World world, LivingPet pet) {
        super(world, pet);
        this.a(0.6F, 0.7F);
        this.fireProof = true;
    }

    public void setOnFire(boolean flag) {
        this.datawatcher.watch(16, (byte) (flag ? 1 : 0));
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, new Byte((byte) 0));
    }

    @Override
    protected String getIdleSound() {
        return "mob.blaze.breathe";
    }

    @Override
    protected String getDeathSound() {
        return "mob.blaze.death";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.REGULAR;
    }

    @Override
    public void onLive() {
        super.onLive();
        if (this.random.nextBoolean() && particle <= 0 && !this.isInvisible()) {
            try {
                Particle.FIRE.sendTo(pet.getLocation());
                Particle.SMOKE.sendTo(pet.getLocation());
            } catch (Exception e) {
                Logger.log(Logger.LogLevel.WARNING, "Particle effect creation failed.", e, true);
            }
        }
    }
}
