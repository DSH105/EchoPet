package io.github.dsh105.echopet.util;

import net.minecraft.server.v1_7_R2.ChunkCoordinates;
import net.minecraft.server.v1_7_R2.ControllerJump;
import net.minecraft.server.v1_7_R2.ControllerLook;
import net.minecraft.server.v1_7_R2.ControllerMove;
import net.minecraft.server.v1_7_R2.EntityCreature;
import net.minecraft.server.v1_7_R2.EntityInsentient;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.EntityPlayer;
import net.minecraft.server.v1_7_R2.EntitySenses;
import net.minecraft.server.v1_7_R2.MathHelper;
import net.minecraft.server.v1_7_R2.Navigation;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

/*
 * From EntityAPI :)
 */

public class NMSEntityUtil {

    public static Navigation getNavigation(LivingEntity livingEntity) {
        if (livingEntity instanceof CraftLivingEntity) {
            return getNavigation(((CraftLivingEntity) livingEntity).getHandle());
        }
        return null;
    }

    public static Navigation getNavigation(EntityLiving entityLiving) {
        if (entityLiving instanceof EntityInsentient) {
            return ((EntityInsentient) entityLiving).getNavigation();
        }
        return null;
    }

    public static EntitySenses getEntitySenses(LivingEntity livingEntity) {
        if (livingEntity instanceof CraftLivingEntity) {
            return getEntitySenses(((CraftLivingEntity) livingEntity).getHandle());
        }
        return null;
    }

    public static EntitySenses getEntitySenses(EntityLiving entityLiving) {
        if (entityLiving instanceof EntityInsentient) {
            return ((EntityInsentient) entityLiving).getEntitySenses();
        }
        return null;
    }

    public static ControllerJump getControllerJump(LivingEntity livingEntity) {
        if (livingEntity instanceof CraftLivingEntity) {
            return getControllerJump(((CraftLivingEntity) livingEntity).getHandle());
        }
        return null;
    }

    public static ControllerJump getControllerJump(EntityLiving entityLiving) {
        if (entityLiving instanceof EntityInsentient) {
            return ((EntityInsentient) entityLiving).getControllerJump();
        }
        return null;
    }

    public static ControllerMove getControllerMove(LivingEntity livingEntity) {
        if (livingEntity instanceof CraftLivingEntity) {
            return getControllerMove(((CraftLivingEntity) livingEntity).getHandle());
        }
        return null;
    }

    public static ControllerMove getControllerMove(EntityLiving entityLiving) {
        if (entityLiving instanceof EntityInsentient) {
            return ((EntityInsentient) entityLiving).getControllerMove();
        }
        return null;
    }

    public static ControllerLook getControllerLook(LivingEntity livingEntity) {
        if (livingEntity instanceof CraftLivingEntity) {
            return getControllerLook(((CraftLivingEntity) livingEntity).getHandle());
        }
        return null;
    }

    public static ControllerLook getControllerLook(EntityLiving entityLiving) {
        if (entityLiving instanceof EntityInsentient) {
            return ((EntityInsentient) entityLiving).getControllerLook();
        }
        return null;
    }

    public static boolean hasGuardedArea(EntityLiving entityLiving) {
        if (entityLiving instanceof EntityCreature) {
            return ((EntityCreature) entityLiving).bY();
        } else return false;
    }

    public static boolean isInGuardedAreaOf(EntityLiving entityLiving) {
        if (entityLiving instanceof EntityCreature) {
            return ((EntityCreature) entityLiving).bS();
        } else return false;
    }

    public static boolean isInGuardedAreaOf(EntityLiving entityLiving, int x, int y, int z) {
        if (entityLiving instanceof EntityCreature) {
            return ((EntityCreature) entityLiving).b(x, y, z);
        } else return false;
    }

    public static float getRangeOfGuardedAreaFor(EntityLiving entityLiving) {
        if (entityLiving instanceof EntityCreature) {
            return ((EntityCreature) entityLiving).bW();
        } else return 1.0F;
    }

    public static ChunkCoordinates getChunkCoordinates(EntityLiving inEntity) {
        if (inEntity instanceof EntityCreature) {
            return ((EntityCreature) inEntity).bV();
        } else if (inEntity instanceof EntityPlayer) {
            return ((EntityPlayer) inEntity).getChunkCoordinates();
        } else {
            return new ChunkCoordinates(MathHelper.floor(inEntity.locX), MathHelper.floor(inEntity.locY), MathHelper.floor(inEntity.locZ));
        }
    }
}
