package io.github.dsh105.echopet.entity.living.type.ocelot;

import io.github.dsh105.echopet.entity.living.EntityAgeablePet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import net.minecraft.server.v1_7_R1.World;
import org.bukkit.entity.Ocelot.Type;

public class EntityOcelotPet extends EntityAgeablePet {

    public EntityOcelotPet(World world) {
        super(world);
    }

    public EntityOcelotPet(World world, LivingPet pet) {
        super(world, pet);
        this.a(0.9F, 0.9F);
        this.fireProof = true;
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