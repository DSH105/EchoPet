package io.github.dsh105.echopet.entity.type.snowman;

import com.dsh105.dshutils.Particle;
import io.github.dsh105.echopet.entity.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.v1_7_R1.World;

@EntitySize(width = 0.4F, height = 1.8F)
@EntityPetType(petType = PetType.SNOWMAN)
public class EntitySnowmanPet extends EntityPet {

    public EntitySnowmanPet(World world) {
        super(world);
    }

    public EntitySnowmanPet(World world, Pet pet) {
        super(world, pet);
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
            } catch (Exception ex) {
                Logger.getLogger(EntitySnowmanPet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
