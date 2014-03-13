package io.github.dsh105.echopet.entity.type.witch;

import com.dsh105.dshutils.Particle;
import io.github.dsh105.echopet.entity.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.v1_7_R1.World;

@EntitySize(width = 0.6F, height = 0.8F)
@EntityPetType(petType = PetType.WITCH)
public class EntityWitchPet extends EntityPet {

    public EntityWitchPet(World world) {
        super(world);
    }

    public EntityWitchPet(World world, Pet pet) {
        super(world, pet);
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
            } catch (Exception ex) {
                Logger.getLogger(EntityWitchPet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
