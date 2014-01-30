package io.github.dsh105.echopet.entity.living.type.skeleton;

import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.entity.SizeCategory;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import net.minecraft.server.v1_7_R1.ItemStack;
import net.minecraft.server.v1_7_R1.Items;
import net.minecraft.server.v1_7_R1.World;
import org.bukkit.scheduler.BukkitRunnable;

public class EntitySkeletonPet extends EntityLivingPet {

    public EntitySkeletonPet(World world) {
        super(world);
    }

    public EntitySkeletonPet(World world, LivingPet pet) {
        super(world, pet);
        this.a(0.6F, 0.6F);
        this.fireProof = true;
        final SkeletonPet sp = (SkeletonPet) pet;
        new BukkitRunnable() {
            public void run() {
                if (sp.wither) {
                    setEquipment(0, new ItemStack(Items.STONE_SWORD));
                } else {
                    setEquipment(0, new ItemStack(Items.BOW));
                }
            }
        }.runTaskLater(EchoPetPlugin.getInstance(), 5L);
    }

    public void setWither(boolean flag) {
        this.datawatcher.watch(13, (byte) (flag ? 1 : 0));
        if (flag) {
            setEquipment(0, new ItemStack(Items.STONE_SWORD));
        } else {
            setEquipment(0, new ItemStack(Items.BOW));
        }
    }

    public int getSkeletonType() {
        return this.datawatcher.getByte(13);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(13, new Byte((byte) 0));
    }

    @Override
    protected String getIdleSound() {
        return "mob.skeleton.say";
    }

    @Override
    protected void makeStepSound() {
        this.makeSound("mob.skeleton.step", 0.15F, 1.0F);
    }

    @Override
    protected String getDeathSound() {
        return "mob.skeleton.death";
    }

    @Override
    public SizeCategory getSizeCategory() {
        if (this.getSkeletonType() == 1) {
            return SizeCategory.LARGE;
        } else {
            return SizeCategory.REGULAR;
        }
    }
}