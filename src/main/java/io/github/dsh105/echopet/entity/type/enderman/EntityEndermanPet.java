package io.github.dsh105.echopet.entity.type.enderman;

import com.dsh105.dshutils.Particle;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.EntitySize;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.entity.SizeCategory;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.v1_7_R2.Block;
import net.minecraft.server.v1_7_R2.World;

@EntitySize(width = 0.6F, height = 2.9F)
@EntityPetType(petType = PetType.ENDERMAN)
public class EntityEndermanPet extends EntityPet {

    public EntityEndermanPet(World world) {
        super(world);
    }

    public EntityEndermanPet(World world, Pet pet) {
        super(world, pet);
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
            } catch (Exception ex) {
                Logger.getLogger(EntityEndermanPet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
