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

package com.dsh105.echopet.compat.nms.v1_9_R1;

import static com.captainbern.reflection.matcher.Matchers.withArguments;
import static com.captainbern.reflection.matcher.Matchers.withType;

import java.util.List;
import java.util.Set;

import org.bukkit.craftbukkit.v1_9_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

import com.captainbern.minecraft.reflection.MinecraftReflection;
import com.captainbern.reflection.ClassTemplate;
import com.captainbern.reflection.Reflection;
import com.captainbern.reflection.SafeField;
import com.captainbern.reflection.SafeMethod;
import com.captainbern.reflection.accessor.FieldAccessor;
import com.captainbern.reflection.accessor.MethodAccessor;

import net.minecraft.server.v1_9_R1.*;

/*
 * From EntityAPI :)
 */

public class NMSEntityUtil {

    public static NavigationAbstract getNavigation(LivingEntity livingEntity) {
        if (livingEntity instanceof CraftLivingEntity) {
            return getNavigation(((CraftLivingEntity) livingEntity).getHandle());
        }
        return null;
    }

    public static NavigationAbstract getNavigation(EntityLiving entityLiving) {
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

    public static boolean isInGuardedAreaOf(EntityLiving entityLiving, int x, int y, int z) {
        // TODO: not used currently
        return false;
        /*if (entityLiving instanceof EntityCreature) {
            return ((EntityCreature) entityLiving).d(new BlockPosition(x, y, z));
        } else {
            return false;
        }*/
    }

    /*
     * Hacky stuff to get around doTick() becoming final
     */
    
	protected static FieldAccessor<Set> GOALS;
	protected static FieldAccessor<Set> ACTIVE_GOALS;

    protected static MethodAccessor<Void> ADD_GOAL;

    protected static FieldAccessor<Object> GOAL_SELECTOR;
    
    public static void clearGoals(Object nmsEntityHandle) {
        if (GOALS == null || ACTIVE_GOALS == null || GOAL_SELECTOR == null) {
            initializeFields();
        }

        GOALS.get(GOAL_SELECTOR.get(nmsEntityHandle)).clear();
        ACTIVE_GOALS.get(GOAL_SELECTOR.get(nmsEntityHandle)).clear();
    }

    protected static void initializeFields() {
        try {

            ClassTemplate goalTemplate = new Reflection().reflect(MinecraftReflection.getMinecraftClass("PathfinderGoalSelector"));

            List<SafeMethod<Void>> methodCandidates = goalTemplate.getSafeMethods(withArguments(int.class, MinecraftReflection.getMinecraftClass("PathfinderGoal")));
            if (methodCandidates.size() > 0) {
                ADD_GOAL = methodCandidates.get(0).getAccessor();
            } else {
                throw new RuntimeException("Failed to get the addGoal method!");
            }

			List<SafeField<Set>> fieldCandidates = goalTemplate.getSafeFields(withType(Set.class));
            if (fieldCandidates.size() > 1) {
                GOALS = fieldCandidates.get(0).getAccessor();
                ACTIVE_GOALS = fieldCandidates.get(0).getAccessor();
            } else {
                throw new RuntimeException("Failed to initialize the goal-lists!");
            }

            // The GoalSelector
            ClassTemplate entityTemplate = new Reflection().reflect(MinecraftReflection.getMinecraftClass("EntityInsentient"));
            List<SafeField<Object>> candidates = entityTemplate.getSafeFields(withType(goalTemplate.getReflectedClass()));

            if (candidates.size() > 0) {
                GOAL_SELECTOR = candidates.get(0).getAccessor(); // the normal selector is the first one
            } else {
                throw new RuntimeException("Failed to initialize the GoalSelector field for the entities");
            }

        } catch (Exception ಠ_ಠ) {
            throw new RuntimeException("Failed to initialize the goal-related fields!", ಠ_ಠ);
        }
    }
}
