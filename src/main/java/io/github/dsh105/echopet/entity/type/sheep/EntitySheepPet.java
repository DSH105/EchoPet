package io.github.dsh105.echopet.entity.type.sheep;

import io.github.dsh105.echopet.entity.EntityAgeablePet;
import io.github.dsh105.echopet.entity.EntitySize;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import net.minecraft.server.v1_7_R1.World;

@EntitySize(width = 0.9F, height = 1.3F)
public class EntitySheepPet extends EntityAgeablePet {

    public EntitySheepPet(World world) {
        super(world);
    }

    public EntitySheepPet(World world, Pet pet) {
        super(world, pet);
    }

    @Override
    public PetType getEntityPetType() {
        return PetType.SHEEP;
    }

    public int getColor() {
        return this.datawatcher.getByte(16) & 15;
    }

    public void setColor(int i) {
        byte b0 = this.datawatcher.getByte(16);

        byte b = Byte.valueOf((byte) (b0 & 240 | i & 15));
        this.datawatcher.watch(16, b);
    }

    public boolean isSheared() {
        return (this.datawatcher.getByte(16) & 16) != 0;
    }

    public void setSheared(boolean flag) {
        byte b0 = this.datawatcher.getByte(16);

        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 16)));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & -17)));
        }
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, new Byte((byte) 0));
    }

    @Override
    protected void makeStepSound() {
        this.makeSound("mob.sheep.step", 0.15F, 1.0F);
    }

    @Override
    protected String getIdleSound() {
        return "mob.sheep.say";
    }

    @Override
    protected String getDeathSound() {
        return "mob.sheep.say";
    }
}