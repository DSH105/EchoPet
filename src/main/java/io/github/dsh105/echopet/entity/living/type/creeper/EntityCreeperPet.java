package io.github.dsh105.echopet.entity.living.type.creeper;

import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import io.github.dsh105.echopet.entity.living.SizeCategory;
import io.github.dsh105.echopet.logger.Logger;
import io.github.dsh105.echopet.util.Particle;
import net.minecraft.server.v1_6_R3.World;

public class EntityCreeperPet extends EntityLivingPet {

    public EntityCreeperPet(World world) {
        super(world);
    }

    public EntityCreeperPet(World world, LivingPet pet) {
        super(world, pet);
        this.a(0.6F, 0.9F);
        this.fireProof = true;
    }

    public void setPowered(boolean flag) {
        this.datawatcher.watch(17, Byte.valueOf((byte) (flag ? 1 : 0)));
        ((CreeperPet) pet).powered = flag;
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, Byte.valueOf((byte) -1));
        this.datawatcher.a(17, Byte.valueOf((byte) 0));
    }

    @Override
    protected String getIdleSound() {
        return "";
    }

    @Override
    protected String getDeathSound() {
        return "mob.creeper.death";
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
                Particle.SMOKE.sendToLocation(pet.getLocation());
            } catch (Exception e) {
                Logger.log(Logger.LogLevel.WARNING, "Particle effect creation failed.", e, true);
            }
        }
    }
}
