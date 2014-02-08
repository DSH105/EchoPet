package io.github.dsh105.echopet.entity.type.slime;

import com.dsh105.dshutils.Particle;
import io.github.dsh105.echopet.entity.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.v1_7_R1.World;

@EntitySize(width = 0.6F, height = 0.6F)
public class EntitySlimePet extends EntityPet {

    public EntitySlimePet(World world) {
        super(world);
    }

    int jumpDelay;

    public EntitySlimePet(World world, Pet pet) {
        super(world, pet);
        int i = 1 << this.random.nextInt(3);
        this.setSize(i);
        this.jumpDelay = this.random.nextInt(15) + 10;
    }

    @Override
    public PetType getEntityPetType() {
        return PetType.SLIME;
    }

    public void setSize(int i) {
        this.datawatcher.watch(16, new Byte((byte) i));
        EntitySize es = this.getClass().getAnnotation(EntitySize.class);
        this.a(es.width() * (float) i, es.height() * (float) i);
        this.setPosition(this.locX, this.locY, this.locZ);
        this.setHealth(this.getMaxHealth());
    }

    public int getSize() {
        return this.datawatcher.getByte(16);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, new Byte((byte) 1));
    }

    @Override
    protected String getIdleSound() {
        return "";
    }

    @Override
    protected String getDeathSound() {
        return "mob.slime." + (this.getSize() > 1 ? "big" : "small");
    }

    @Override
    public void onLive() {
        super.onLive();

        if (this.onGround && this.jumpDelay-- <= 0) {
            this.jumpDelay = this.random.nextInt(15) + 10;
            this.makeSound(this.getDeathSound(), this.bf(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            getControllerJump().a();
        }

        if (this.random.nextBoolean() && particle <= 0 && !this.isInvisible()) {
            try {
                Particle.SLIME_SPLAT.sendTo(pet.getLocation());
            } catch (Exception ex) {
                Logger.getLogger(EntitySlimePet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public SizeCategory getSizeCategory() {
        if (this.getSize() == 1) {
            return SizeCategory.TINY;
        } else if (this.getSize() == 4) {
            return SizeCategory.LARGE;
        } else {
            return SizeCategory.REGULAR;
        }
    }
}