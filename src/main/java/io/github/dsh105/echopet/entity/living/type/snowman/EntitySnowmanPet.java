package io.github.dsh105.echopet.entity.living.type.snowman;

import io.github.dsh105.dshutils.Particle;
import io.github.dsh105.echopet.entity.living.EntityLivingPet;
import io.github.dsh105.echopet.entity.living.LivingPet;
import io.github.dsh105.echopet.entity.living.SizeCategory;
import io.github.dsh105.dshutils.logger.Logger;
import net.minecraft.server.v1_7_R1.Material;
import net.minecraft.server.v1_7_R1.MathHelper;
import net.minecraft.server.v1_7_R1.World;
import org.bukkit.craftbukkit.v1_7_R1.util.CraftMagicNumbers;

public class EntitySnowmanPet extends EntityLivingPet {

    public EntitySnowmanPet(World world) {
        super(world);
    }

    public EntitySnowmanPet(World world, LivingPet pet) {
        super(world, pet);
        this.a(0.4F, 1.8F);
        this.fireProof = true;
    }

    @Override
    protected String getIdleSound() {
        return "none";
    }

    @Override
    protected String getDeathSound() {
        return "none";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.REGULAR;
    }

    @Override
    public void onLive() {
        super.onLive();

        for (int l = 0; l < 4; ++l) {
            int i = MathHelper.floor(this.locX + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
            int j = MathHelper.floor(this.locY);
            int k = MathHelper.floor(this.locZ + (double) ((float) (l / 2 % 2 * 2 - 1) * 0.25F));
            org.bukkit.block.BlockState blockState = this.world.getWorld().getBlockAt(j, k, l).getState();
            if (blockState.getType().equals(Material.SNOW_LAYER)) {
                blockState.setType(CraftMagicNumbers.getMaterial(net.minecraft.server.v1_7_R1.Blocks.AIR));
            }
        }

        if (this.random.nextBoolean() && particle <= 0 && !this.isInvisible()) {
            try {
                Particle.SNOW_SHOVEL.sendTo(pet.getLocation());
            } catch (Exception e) {
                Logger.log(Logger.LogLevel.WARNING, "Particle effect creation failed.", e, true);
            }
        }
    }
}