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

package com.dsh105.echopet.nms.v1_7_R4;

import net.minecraft.server.v1_7_R4.*;

/*
 * From EntityAPI :)
 */

public class NMSEntityUtil {


    public static Navigation getNavigation(EntityLiving entityLiving) {
        if (entityLiving instanceof EntityInsentient) {
            return ((EntityInsentient) entityLiving).getNavigation();
        }
        return null;
    }

    public static EntitySenses getEntitySenses(EntityLiving entityLiving) {
        if (entityLiving instanceof EntityInsentient) {
            return ((EntityInsentient) entityLiving).getEntitySenses();
        }
        return null;
    }

    public static ControllerJump getControllerJump(EntityLiving entityLiving) {
        if (entityLiving instanceof EntityInsentient) {
            return ((EntityInsentient) entityLiving).getControllerJump();
        }
        return null;
    }

    public static ControllerMove getControllerMove(EntityLiving entityLiving) {
        if (entityLiving instanceof EntityInsentient) {
            return ((EntityInsentient) entityLiving).getControllerMove();
        }
        return null;
    }

    public static ControllerLook getControllerLook(EntityLiving entityLiving) {
        if (entityLiving instanceof EntityInsentient) {
            return ((EntityInsentient) entityLiving).getControllerLook();
        }
        return null;
    }

    public static int getMaxHeadRotation(EntityLiving entityLiving) {
        if (entityLiving instanceof EntityInsentient) {
            return ((EntityInsentient) entityLiving).x();
        }
        return 40;
    }
}
