/*
 * This file is part of EchoPet.
 *
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.nms.v1_7_R2;

import net.minecraft.server.v1_7_R2.*;
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
        return entityLiving instanceof EntityCreature && ((EntityCreature) entityLiving).bY();
    }

    public static boolean isInGuardedAreaOf(EntityLiving entityLiving) {
        return entityLiving instanceof EntityCreature && ((EntityCreature) entityLiving).bS();
    }

    public static boolean isInGuardedAreaOf(EntityLiving entityLiving, int x, int y, int z) {
        return entityLiving instanceof EntityCreature && ((EntityCreature) entityLiving).b(x, y, z);
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

    public static int getMaxHeadRotation(EntityLiving entityLiving) {
        if (entityLiving instanceof EntityInsentient) {
            return ((EntityInsentient) entityLiving).bv();
        }
        return 40;
    }
}
