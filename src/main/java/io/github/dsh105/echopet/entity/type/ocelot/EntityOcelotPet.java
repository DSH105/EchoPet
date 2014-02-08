package io.github.dsh105.echopet.entity.type.ocelot;

import io.github.dsh105.echopet.entity.*;
import net.minecraft.server.v1_7_R1.World;

@EntitySize(width = 0.9F, height = 0.9F)
@EntityPetType(petType = PetType.OCELOT)
public class EntityOcelotPet extends EntityAgeablePet {

    public EntityOcelotPet(World world) {
        super(world);
    }

    public EntityOcelotPet(World world, Pet pet) {
        super(world, pet);
    }

    public int getCatType() {
        return this.datawatcher.getByte(18);
    }

    public void setCatType(int i) {
        this.datawatcher.watch(18, (byte) i);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, new Byte((byte) 0));
        this.datawatcher.a(18, new Byte((byte) 0));
    }

    @Override
    protected void makeStepSound() {
        this.makeSound("mob.ozelot.step", 0.15F, 1.0F);
    }

    @Override
    protected String getIdleSound() {
        return (this.random.nextInt(4) == 0 ? "mob.cat.purreow" : "mob.cat.meow");
    }

    @Override
    protected String getDeathSound() {
        return "mob.cat.hitt";
    }
}