package io.github.dsh105.echopet.entity.living.type.squid;

import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import io.github.dsh105.echopet.entity.living.SizeCategory;
import io.github.dsh105.echopet.logger.Logger;
import io.github.dsh105.echopet.util.Particle;
import net.minecraft.server.v1_7_R1.World;

public class EntitySquidPet extends EntityLivingPet {

    public EntitySquidPet(World world) {
        super(world);
    }

    public EntitySquidPet(World world, LivingPet pet) {
        super(world, pet);
        this.a(0.95F, 0.95F);
        this.fireProof = true;
    }

    @Override
    protected String getIdleSound() {
        return "";
    }

    @Override
    protected String getDeathSound() {
        return null;
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.REGULAR;
    }

    @Override
    public void onLive() {
        super.onLive();
        if (this.random.nextBoolean() && particle <= 0 && !this.isInvisible()) {
            if (this.M()) {
                try {
                    Particle.BUBBLE.sendTo(pet.getLocation());
                } catch (Exception e) {
                    Logger.log(Logger.LogLevel.WARNING, "Particle effect creation failed.", e, true);
                }
            }
            try {
                Particle.SPLASH.sendTo(pet.getLocation());
            } catch (Exception e) {
                Logger.log(Logger.LogLevel.WARNING, "Particle effect creation failed.", e, true);
            }
        }
    }
}