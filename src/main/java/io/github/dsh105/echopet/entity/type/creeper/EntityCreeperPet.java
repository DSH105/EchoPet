package io.github.dsh105.echopet.entity.type.creeper;

import com.dsh105.dshutils.Particle;
import io.github.dsh105.echopet.entity.*;
import net.minecraft.server.v1_7_R1.World;

@EntitySize(width = 0.6F, height = 0.9F)
@EntityPetType(petType = PetType.CREEPER)
public class EntityCreeperPet extends EntityPet {

    public EntityCreeperPet(World world) {
        super(world);
    }

    public EntityCreeperPet(World world, Pet pet) {
        super(world, pet);
    }

    public void setPowered(boolean flag) {
        this.datawatcher.watch(17, Byte.valueOf((byte) (flag ? 1 : 0)));
    }

    public void setIgnited(boolean flag) {
        this.datawatcher.watch(18, Byte.valueOf((byte) (flag ? 1 : 0)));
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, Byte.valueOf((byte) -1));
        this.datawatcher.a(17, Byte.valueOf((byte) 0));
        this.datawatcher.a(18, Byte.valueOf((byte) 0));
    }

    @Override
    protected String getIdleSound() {
        return "";
    }

    @Override
    protected String getDeathSound() {
        return "mob.creeper.death";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.REGULAR;
    }

    @Override
    public void onLive() {
        super.onLive();
        if (this.random.nextBoolean() && particle <= 0 && !this.isInvisible()) {
            Particle.SMOKE.sendTo(pet.getLocation());
        }
    }
}
