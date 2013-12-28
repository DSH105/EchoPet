package io.github.dsh105.echopet.entity.living.type.snowman;

import io.github.dsh105.dshutils.Particle;
import io.github.dsh105.dshutils.logger.Logger;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import io.github.dsh105.echopet.entity.living.SizeCategory;
import net.minecraft.server.v1_7_R1.World;

public class EntitySnowmanPet extends EntityLivingPet {

    public EntitySnowmanPet(World world) {
        super(world);
    }

    public EntitySnowmanPet(World world, LivingPet pet) {
        super(world, pet);
        this.a(0.4F, 1.8F);
        this.fireProof = true;
    }

    @Override
    protected String getIdleSound() {
        return "none";
    }

    @Override
    protected String getDeathSound() {
        return "none";
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
                Particle.SNOW_SHOVEL.sendTo(pet.getLocation());
            } catch (Exception e) {
                Logger.log(Logger.LogLevel.WARNING, "Particle effect creation failed.", e, true);
            }
        }
    }
}