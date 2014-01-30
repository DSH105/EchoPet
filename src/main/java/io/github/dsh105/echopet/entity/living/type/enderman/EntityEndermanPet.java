package io.github.dsh105.echopet.entity.living.type.enderman;

import com.dsh105.dshutils.Particle;
import com.dsh105.dshutils.logger.Logger;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import io.github.dsh105.echopet.entity.SizeCategory;
import net.minecraft.server.v1_7_R1.Block;
import net.minecraft.server.v1_7_R1.World;

public class EntityEndermanPet extends EntityLivingPet {

    public EntityEndermanPet(World world) {
        super(world);
    }

    public EntityEndermanPet(World world, LivingPet pet) {
        super(world, pet);
        this.a(0.6F, 0.9F);
        this.fireProof = true;
    }

    public void setScreaming(boolean flag) {
        this.datawatcher.watch(18, Byte.valueOf((byte) (flag ? 1 : 0)));
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, new Byte((byte) 0));
        this.datawatcher.a(17, new Byte((byte) 0));
        this.datawatcher.a(18, new Byte((byte) 0));
    }

    @Override
    protected String getIdleSound() {
        return this.isScreaming() ? "mob.endermen.scream" : "mob.endermen.idle";
    }

    public boolean isScreaming() {
        return this.datawatcher.getByte(18) > 0;
    }

    public void setCarried(Block block) {
        this.datawatcher.watch(16, Byte.valueOf((byte) (Block.b(block) & 255)));
    }

    public Block getCarried() {
        return Block.e(this.datawatcher.getByte(16));
    }

    public void setCarriedData(int i) {
        this.datawatcher.watch(17, Byte.valueOf((byte) (i & 255)));
    }

    public int getCarriedData() {
        return this.datawatcher.getByte(17);
    }

    @Override
    protected String getDeathSound() {
        return "mob.enderman.death";
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
                Particle.PORTAL.sendTo(pet.getLocation());
            } catch (Exception e) {
                Logger.log(Logger.LogLevel.WARNING, "Particle effect creation failed.", e, true);
            }
        }
    }
}
