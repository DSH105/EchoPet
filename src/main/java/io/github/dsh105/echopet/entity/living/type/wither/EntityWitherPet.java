package io.github.dsh105.echopet.entity.living.type.wither;

import com.dsh105.dshutils.Particle;
import com.dsh105.dshutils.logger.Logger;
import io.github.dsh105.echopet.entity.SizeCategory;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import net.minecraft.server.v1_7_R1.World;

public class EntityWitherPet extends EntityLivingPet {

    public EntityWitherPet(World world) {
        super(world);
    }

    public EntityWitherPet(World world, LivingPet pet) {
        super(world, pet);
        this.a(0.9F, 4.0F);
        this.fireProof = true;
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(17, new Integer(0));
        this.datawatcher.a(18, new Integer(0));
        this.datawatcher.a(19, new Integer(0));
        this.datawatcher.a(20, new Integer(0));
    }

    public void setShielded(boolean flag) {
        this.datawatcher.watch(20, new Integer((flag ? 1 : 0)));
        this.setHealth((float) (flag ? 150 : 300));
        ((WitherPet) pet).shield = flag;
    }

    @Override
    protected String getIdleSound() {
        return "mob.wither.idle";
    }

    @Override
    protected String getDeathSound() {
        return "mob.wither.death";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.LARGE;
    }

    @Override
    public void onLive() {
        super.onLive();
        if (this.random.nextBoolean() && particle <= 0 && !this.isInvisible()) {
            try {
                Particle.VOID.sendTo(pet.getLocation());
            } catch (Exception e) {
                Logger.log(Logger.LogLevel.WARNING, "Particle effect creation failed.", e, true);
            }
        }
    }
}