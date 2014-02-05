package io.github.dsh105.echopet.entity.type.spider;

import com.dsh105.dshutils.Particle;
import io.github.dsh105.echopet.entity.*;
import net.minecraft.server.v1_7_R1.World;

@EntitySize(width = 1.4F, height = 0.9F)
public class EntitySpiderPet extends EntityPet {

    public EntitySpiderPet(World world) {
        super(world);
    }

    public EntitySpiderPet(World world, Pet pet) {
        super(world, pet);
    }

    @Override
    public PetType getEntityPetType() {
        return PetType.SPIDER;
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
            Particle.SPELL_AMBIENT.sendTo(pet.getLocation());
        }
    }
}
