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

package com.dsh105.echopet.compat.nms.v1_8_R1.entity.ai;

import com.dsh105.echopet.compat.api.ai.PetGoal;
import com.dsh105.echopet.compat.api.ai.PetGoalType;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.nms.v1_8_R1.NMSEntityUtil;
import com.dsh105.echopet.compat.nms.v1_8_R1.entity.EntityPet;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftLivingEntity;

/**
 * Base target PetGoal
 * <p/>
 * TODO: finish this
 */

public abstract class PetGoalTarget extends PetGoal {

    protected IPet pet;
    protected EntityLiving handle;
    private boolean checkSenses;
    private boolean useMelee;
    private int shouldAttack;
    private int ticksAfterLastAttack;
    private int targetNotVisibleTicks;

    public PetGoalTarget(IPet pet, boolean checkSenses) {
        this(pet, checkSenses, false);
    }

    public PetGoalTarget(IPet pet, boolean checkSenses, boolean useMelee) {
        this.pet = pet;
        this.handle = (EntityPet) pet.getEntityPet();
        this.checkSenses = checkSenses;
        this.useMelee = false;
    }

    @Override
    public PetGoalType getType() {
        return PetGoalType.ZERO;
    }

    @Override
    public String getDefaultKey() {
        return "Target";
    }

    @Override
    public boolean shouldContinue() {
        EntityLiving entityliving = ((CraftLivingEntity) this.pet.getEntityPet().getTarget()).getHandle();

        if (entityliving == null) {
            return false;
        } else if (!entityliving.isAlive()) {
            return false;
        } else {
            AttributeInstance attribute = this.handle.getAttributeInstance(GenericAttributes.b);
            double range = attribute == null ? 16.0D : attribute.getValue();

            if (this.handle.h(entityliving) > range * range) {
                return false;
            } else {
                if (this.checkSenses) {
                    if (NMSEntityUtil.getEntitySenses(this.handle).a(entityliving)) {
                        this.targetNotVisibleTicks = 0;
                    } else if (++this.targetNotVisibleTicks > 60) {
                        return false;
                    }
                }

                return !(entityliving instanceof EntityPlayer) || !((EntityPlayer) entityliving).playerInteractManager.isCreative();
            }
        }
    }

    @Override
    public void start() {
        this.shouldAttack = 0;
        this.ticksAfterLastAttack = 0;
        this.targetNotVisibleTicks = 0;
    }

    @Override
    public void finish() {
        this.pet.getEntityPet().setTarget(null);
    }

    public boolean isSuitableTarget(EntityLiving entityliving, boolean flag) {
        if (entityliving == null) {
            return false;
        } else if (entityliving == this.handle) {
            return false;
        } else if (!entityliving.isAlive()) {
            return false;
        } else if (!this.canAttackClass(entityliving.getClass())) {
            return false;
        } else {
            String uuid = ((EntityOwnable) this.handle).getOwnerUUID();
            boolean hasOwner = !(uuid == null || uuid.length() == 0);
            if (this.handle instanceof EntityOwnable && hasOwner) {
                if (entityliving instanceof EntityOwnable && ((EntityOwnable) this.handle).getOwnerUUID().equals(((EntityOwnable) entityliving).getOwnerUUID())) {
                    return false;
                }

                if (entityliving == ((EntityOwnable) this.handle).getOwner()) {
                    return false;
                }
            } else if (entityliving instanceof EntityHuman && !flag && ((EntityHuman) entityliving).abilities.isInvulnerable) {
                return false;
            }

            if (!NMSEntityUtil.isInGuardedAreaOf(this.handle, MathHelper.floor(entityliving.locX), MathHelper.floor(entityliving.locY), MathHelper.floor(entityliving.locZ))) {
                return false;
            } else if (this.checkSenses && !NMSEntityUtil.getEntitySenses(this.handle).a(entityliving)) {
                return false;
            } else {
                if (this.useMelee) {
                    if (--this.ticksAfterLastAttack <= 0) {
                        this.shouldAttack = 0;
                    }

                    if (this.shouldAttack == 0) {
                        this.shouldAttack = this.attack(entityliving) ? 1 : 2;
                    }

                    if (this.shouldAttack == 2) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    private boolean attack(EntityLiving entityliving) {
        this.ticksAfterLastAttack = 10 + ((EntityPet) this.handle).random().nextInt(5);
        PathEntity pathentity = NMSEntityUtil.getNavigation(this.handle).a(entityliving);

        if (pathentity == null) {
            return false;
        } else {
            PathPoint pathpoint = pathentity.c();

            if (pathpoint == null) {
                return false;
            } else {
                int i = pathpoint.a - MathHelper.floor(entityliving.locX);
                int j = pathpoint.c - MathHelper.floor(entityliving.locZ);

                return (double) (i * i + j * j) <= 2.25D;
            }
        }
    }

    private boolean canAttackClass(Class oclass) {
        if (this.handle instanceof EntityInsentient) {
            return ((EntityInsentient) this.handle).a(oclass);
        } else {
            return EntityCreeper.class != oclass && EntityGhast.class != oclass;
        }
    }
}
