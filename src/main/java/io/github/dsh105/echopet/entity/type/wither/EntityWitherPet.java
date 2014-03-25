package io.github.dsh105.echopet.entity.type.wither;

import com.dsh105.dshutils.Particle;
import io.github.dsh105.echopet.entity.*;
import net.minecraft.server.v1_7_R2.World;

@EntitySize(width = 0.9F, height = 4.0F)
@EntityPetType(petType = PetType.WITHER)
public class EntityWitherPet extends EntityPet {

    public EntityWitherPet(World world) {
        super(world);
    }

    public EntityWitherPet(World world, Pet pet) {
        super(world, pet);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(17, new Integer(0));
        this.datawatcher.a(18, new Integer(0));
        this.datawatcher.a(19, new Integer(0));
        this.datawatcher.a(20, new Integer(0));
    }

    public void setShielded(boolean flag) {
        this.datawatcher.watch(20, new Integer((flag ? 1 : 0)));
        this.setHealth((float) (flag ? 150 : 300));
        ((WitherPet) pet).shield = flag;
    }

    @Override
    protected String getIdleSound() {
        return "mob.wither.idle";
    }

    @Override
    protected String getDeathSound() {
        return "mob.wither.death";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.LARGE;
    }

    @Override
    public void onLive() {
        super.onLive();
        if (this.random.nextBoolean() && particle <= 0 && !this.isInvisible()) {
            try {
                Particle.VOID.sendTo(pet.getLocation());
            } catch (Exception ex) {
                Logger.getLogger(EntityWitherPet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
