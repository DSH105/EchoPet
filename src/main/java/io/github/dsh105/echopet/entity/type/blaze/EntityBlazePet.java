package io.github.dsh105.echopet.entity.type.blaze;

import com.dsh105.dshutils.Particle;
import io.github.dsh105.echopet.entity.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.v1_7_R1.World;

@EntitySize(width = 0.6F, height = 0.7F)
@EntityPetType(petType = PetType.BLAZE)
public class EntityBlazePet extends EntityPet {

    public EntityBlazePet(World world) {
        super(world);
    }

    public EntityBlazePet(World world, Pet pet) {
        super(world, pet);
    }

    public void setOnFire(boolean flag) {
        this.datawatcher.watch(16, (byte) (flag ? 1 : 0));
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, new Byte((byte) 0));
    }

    @Override
    protected String getIdleSound() {
        return "mob.blaze.breathe";
    }

    @Override
    protected String getDeathSound() {
        return "mob.blaze.death";
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
                Particle.FIRE.sendTo(pet.getLocation());
                Particle.SMOKE.sendTo(pet.getLocation());
            } catch (Exception ex) {
                Logger.getLogger(EntityBlazePet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
