package io.github.dsh105.echopet.entity.living.type.witch;

import com.dsh105.dshutils.Particle;
import com.dsh105.dshutils.logger.Logger;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import io.github.dsh105.echopet.entity.SizeCategory;
import net.minecraft.server.v1_7_R1.World;

public class EntityWitchPet extends EntityLivingPet {

    public EntityWitchPet(World world) {
        super(world);
    }

    public EntityWitchPet(World world, LivingPet pet) {
        super(world, pet);
        this.a(0.6F, 0.8F);
        this.fireProof = true;
    }

    @Override
    protected String getIdleSound() {
        return "mob.witch.idle";
    }

    @Override
    protected String getDeathSound() {
        return "mob.witch.death";
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
                Particle.WITCH_MAGIC.sendTo(pet.getLocation());
            } catch (Exception e) {
                Logger.log(Logger.LogLevel.WARNING, "Particle effect creation failed.", e, true);
            }
        }
    }
}