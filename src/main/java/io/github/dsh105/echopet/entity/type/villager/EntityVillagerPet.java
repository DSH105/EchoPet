package io.github.dsh105.echopet.entity.type.villager;

import com.dsh105.dshutils.Particle;
import io.github.dsh105.echopet.entity.*;
import net.minecraft.server.v1_7_R2.World;

@EntitySize(width = 0.6F, height = 1.8F)
@EntityPetType(petType = PetType.VILLAGER)
public class EntityVillagerPet extends EntityAgeablePet {

    public EntityVillagerPet(World world) {
        super(world);
    }

    public EntityVillagerPet(World world, Pet pet) {
        super(world, pet);
    }

    public void setProfession(int i) {
        this.datawatcher.watch(16, i);
    }

    @Override
    public void setBaby(boolean flag) {
        if (flag) {
            this.datawatcher.watch(12, Integer.valueOf(Integer.MIN_VALUE));
        } else {
            this.datawatcher.watch(12, new Integer(0));
        }
    }

    @Override
    protected String getIdleSound() {
        return this.random.nextBoolean() ? "mob.villager.haggle" : "mob.villager.idle";
    }

    @Override
    protected String getDeathSound() {
        return "mob.villager.death";
    }

    @Override
    public void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, new Integer(0));
    }

    @Override
    public void onLive() {
        super.onLive();
        if (this.random.nextBoolean() && particle <= 0 && !this.isInvisible()) {
            try {
                Particle.SPARKLE.sendTo(pet.getLocation());
            } catch (Exception ex) {
                Logger.getLogger(EntityVillagerPet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
