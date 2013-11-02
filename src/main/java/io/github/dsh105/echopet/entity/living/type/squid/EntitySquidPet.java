package io.github.dsh105.echopet.entity.living.type.squid;

import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import io.github.dsh105.echopet.entity.living.SizeCategory;
import io.github.dsh105.echopet.logger.Logger;
import io.github.dsh105.echopet.util.Particle;
import net.minecraft.server.v1_6_R3.World;

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
            if (pet.getEntityPet().world.getMaterial((int) pet.getEntityPet().locX, (int) pet.getEntityPet().locY, (int) pet.getEntityPet().locZ).isLiquid()) {
                try {
                    Particle.BUBBLE.sendToLocation(pet.getLocation());
                } catch (Exception e) {
                    Logger.log(Logger.LogLevel.WARNING, "Particle effect creation failed.", e, true);
                }
            }
            try {
                Particle.SPLASH.sendToLocation(pet.getLocation());
            } catch (Exception e) {
                Logger.log(Logger.LogLevel.WARNING, "Particle effect creation failed.", e, true);
            }
        }
    }
}