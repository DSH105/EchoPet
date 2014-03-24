package io.github.dsh105.echopet.entity.pathfinder.goals;

import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.pathfinder.PetGoal;
import io.github.dsh105.echopet.entity.pathfinder.PetGoalType;
import io.github.dsh105.echopet.util.NMSEntityUtil;
import net.minecraft.server.v1_7_R2.*;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;

/**
 * Base target PetGoal
 *
 * TODO: finish this
 */

public abstract class PetGoalTarget extends PetGoal {

    protected Pet pet;
    protected EntityLiving handle;
    private boolean checkSenses;
    private boolean useMelee;
    private int shouldAttack;
    private int ticksAfterLastAttack;
    private int targetNotVisibleTicks;

    public PetGoalTarget(Pet pet, boolean checkSenses) {
        this(pet, checkSenses, false);
    }

    public PetGoalTarget(Pet pet, boolean checkSenses, boolean useMelee) {
        this.pet = pet;
        this.handle = pet.getEntityPet();
        this.checkSenses = checkSenses;
        this.useMelee = useMelee;
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
        EntityLiving entityliving = this.pet.getEntityPet().getGoalTarget();

        if (entityliving == null) {
            return false;
        } else if (!entityliving.isAlive()) {
            return false;
        } else {
            AttributeInstance attribute = this.handle.getAttributeInstance(GenericAttributes.b);
            double range = attribute == null ? 16.0D : attribute.getValue();

            if (this.handle.e(entityliving) > range * range) {
                return false;
            } else {
                if (this.checkSenses) {
                    if (NMSEntityUtil.getEntitySenses(this.handle).canSee(entityliving)) {
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
        this.pet.getEntityPet().setGoalTarget(null);
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
            if (this.handle instanceof EntityOwnable && StringUtils.isNotEmpty(((EntityOwnable) this.handle).getOwnerName())) {
                if (entityliving instanceof EntityOwnable && ((EntityOwnable) this.handle).getOwnerName().equals(((EntityOwnable) entityliving).getOwnerName())) {
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
            } else if (this.checkSenses && !NMSEntityUtil.getEntitySenses(this.handle).canSee(entityliving)) {
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
        this.ticksAfterLastAttack = 10 + this.handle.aH().nextInt(5);
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