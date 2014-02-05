package io.github.dsh105.echopet.entity.type.snowman;

import com.dsh105.dshutils.Particle;
import io.github.dsh105.echopet.entity.*;
import net.minecraft.server.v1_7_R1.World;

@EntitySize(width = 0.4F, height = 1.8F)
public class EntitySnowmanPet extends EntityPet {

    public EntitySnowmanPet(World world) {
        super(world);
    }

    public EntitySnowmanPet(World world, Pet pet) {
        super(world, pet);
    }

    @Override
    public PetType getEntityPetType() {
        return PetType.SNOWMAN;
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
            Particle.SNOW_SHOVEL.sendTo(pet.getLocation());
        }
    }
}