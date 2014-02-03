package io.github.dsh105.echopet.entity.living.type.pigzombie;

import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.entity.SizeCategory;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import net.minecraft.server.v1_7_R1.ItemStack;
import net.minecraft.server.v1_7_R1.Items;
import net.minecraft.server.v1_7_R1.World;
import org.bukkit.scheduler.BukkitRunnable;

public class EntityPigZombiePet extends EntityLivingPet {

    public EntityPigZombiePet(World world) {
        super(world);
    }

    public EntityPigZombiePet(World world, LivingPet pet) {
        super(world, pet);
        this.a(0.6F, 0.9F);
        this.fireProof = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                setEquipment(0, new ItemStack(Items.GOLD_SWORD));
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