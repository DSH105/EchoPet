package io.github.dsh105.echopet.entity.type.zombie;

import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.entity.*;
import net.minecraft.server.v1_7_R2.ItemStack;
import net.minecraft.server.v1_7_R2.Items;
import net.minecraft.server.v1_7_R2.World;
import org.bukkit.scheduler.BukkitRunnable;

@EntitySize(width = 0.6F, height = 1.8F)
@EntityPetType(petType = PetType.ZOMBIE)
public class EntityZombiePet extends EntityPet {

    public EntityZombiePet(World world) {
        super(world);
    }

    public EntityZombiePet(World world, Pet pet) {
        super(world, pet);
        new BukkitRunnable() {
            @Override
            public void run() {
                setEquipment(0, new ItemStack(Items.IRON_SPADE));
            }
        }.runTaskLater(EchoPetPlugin.getInstance(), 5L);
    }

    public void setBaby(boolean flag) {
        this.datawatcher.watch(12, (byte) (flag ? 1 : 0));
    }

    public void setVillager(boolean flag) {
        this.datawatcher.watch(13, (byte) (flag ? 1 : 0));
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(12, new Byte((byte) 0));
        this.datawatcher.a(13, new Byte((byte) 0));
    }

    @Override
    protected String getIdleSound() {
        return "mob.zombie.say";
    }

    @Override
    protected void makeStepSound() {
        this.makeSound("mob.zombie.step", 0.15F, 1.0F);
    }

    @Override
    protected String getDeathSound() {
        return "mob.zombie.death";
    }

    @Override
    public boolean isBaby() {
        return this.datawatcher.getByte(12) == 1;
    }

    @Override
    public SizeCategory getSizeCategory() {
        if (this.isBaby()) {
            return SizeCategory.TINY;
        } else {
            return SizeCategory.REGULAR;
        }
    }
}
