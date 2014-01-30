package io.github.dsh105.echopet.entity.living.type.cavespider;

import com.dsh105.dshutils.Particle;
import com.dsh105.dshutils.logger.Logger;
import io.github.dsh105.echopet.entity.SizeCategory;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import net.minecraft.server.v1_7_R1.World;

public class EntityCaveSpiderPet extends EntityLivingPet {

    EntityCaveSpiderPet(World world) {
        super(world);
    }

    public EntityCaveSpiderPet(World world, LivingPet pet) {
        super(world, pet);
        this.a(0.7F, 0.5F);
        this.fireProof = true;
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, new Byte((byte) 0));
    }

    @Override
    protected void makeStepSound() {
        makeSound("mob.spider.step", 0.15F, 1.0F);
    }

    @Override
    protected String getIdleSound() {
        return "mob.spider.say";
    }

    @Override
    protected String getDeathSound() {
        return "mob.spider.death";
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
                Particle.SPELL_AMBIENT.sendTo(pet.getLocation());
            } catch (Exception e) {
                Logger.log(Logger.LogLevel.WARNING, "Particle effect creation failed.", e, true);
            }
        }
    }
}
