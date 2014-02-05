package io.github.dsh105.echopet.entity.type.pigzombie;

import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.entity.*;
import net.minecraft.server.v1_7_R1.ItemStack;
import net.minecraft.server.v1_7_R1.Items;
import net.minecraft.server.v1_7_R1.World;
import org.bukkit.scheduler.BukkitRunnable;

@EntitySize(width = 0.6F, height = 0.9F)
public class EntityPigZombiePet extends EntityPet {

    public EntityPigZombiePet(World world) {
        super(world);
    }

    public EntityPigZombiePet(World world, Pet pet) {
        super(world, pet);
        new BukkitRunnable() {
            @Override
            public void run() {
                setEquipment(0, new ItemStack(Items.GOLD_SWORD));
            }
        }.runTaskLater(EchoPetPlugin.getInstance(), 5L);
    }

    @Override
    public PetType getEntityPetType() {
        return PetType.PIGZOMBIE;
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
        return "mob.zombiepig.zpig";
    }

    @Override
    protected String getDeathSound() {
        return "mob.zombiepig.zpigdeath";
    }

    @Override
    public boolean isBaby() {
        return this.datawatcher.getByte(12) < 0;
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