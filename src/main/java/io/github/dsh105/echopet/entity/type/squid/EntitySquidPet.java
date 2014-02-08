package io.github.dsh105.echopet.entity.type.squid;

import com.dsh105.dshutils.Particle;
import io.github.dsh105.echopet.entity.*;
import net.minecraft.server.v1_7_R1.World;

@EntitySize(width = 0.95F, height = 0.95F)
@EntityPetType(petType = PetType.SQUID)
public class EntitySquidPet extends EntityPet {

    public EntitySquidPet(World world) {
        super(world);
    }

    public EntitySquidPet(World world, Pet pet) {
        super(world, pet);
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
                Particle.BUBBLE.sendTo(pet.getLocation());
            }
            Particle.SPLASH.sendTo(pet.getLocation());
        }
    }
}