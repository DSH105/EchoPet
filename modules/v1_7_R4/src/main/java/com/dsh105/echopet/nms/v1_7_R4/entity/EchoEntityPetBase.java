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

package com.dsh105.echopet.nms.v1_7_R4.entity;

import com.dsh105.echopet.api.config.PetSettings;
import com.dsh105.echopet.api.entity.entitypet.EntityPet;
import com.dsh105.echopet.api.entity.entitypet.EntityPetModifier;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.event.PetAttackEvent;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.nms.v1_7_R4.NMSEntityUtil;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;

public class EchoEntityPetBase<T extends Pet> implements EntityPetModifier<T> {

    private T pet;
    private EntityCreature entity;

    public EchoEntityPetBase(T pet, EchoEntityPetHandle entity) {
        this.pet = pet;
        this.entity = (EntityCreature) entity;
    }

    public EntityCreature getEntity() {
        return entity;
    }

    public EntityPet<T> getEntityPet() {
        return getPet().getEntity();
    }

    public EchoEntityPetHandle getHandle() {
        return (EchoEntityPetHandle) getEntity();
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    @Override
    public boolean useNewAi() {
        return true;
    }

    @Override
    public void onTick() {
        // Check despawn - Persistent entities won't 'age' or despawn when there are no players around
        getEntityPet().checkDespawn();

        // Clear senses
        getHandle().getEntitySenses().a();

        // Update/run our custom AI
        if (getPet().getPetGoalSelector() == null) {
            this.getBukkitEntity().remove();
            return;
        }
        getPet().getPetGoalSelector().updateGoals();

        // Some navigation updates
        getHandle().getNavigation().f();

        // Mob tick
        getEntityPet().mobTick();

        // Other movement updates
        getHandle().getControllerMove().c();
        getHandle().getControllerLook().a();
        getHandle().getControllerJump().b();
    }

    /*
     * Into our EntityPet methods
     */

    @Override
    public CraftEntity getBukkitEntity() {
        return getEntity().getBukkitEntity();
    }

    @Override
    public T getPet() {
        return pet;
    }

    @Override
    public void setMotionX(double motX) {
        getEntity().motX = motX;
    }

    @Override
    public void setMotionY(double motY) {
        getEntity().motY = motY;
    }

    @Override
    public void setMotionZ(double motZ) {
        getEntity().motZ = motZ;
    }

    @Override
    public double getMotionX() {
        return getEntity().motX;
    }

    @Override
    public double getMotionY() {
        return getEntity().motY;
    }

    @Override
    public double getMotionZ() {
        return getEntity().motZ;
    }

    @Override
    public boolean canSee(org.bukkit.entity.Entity entity) {
        return getHandle().getEntitySenses().canSee(((CraftEntity) entity).getHandle());
    }

    @Override
    public boolean isDead() {
        return getEntity().dead;
    }

    @Override
    public boolean inLava() {
        return getEntity().P();
    }

    @Override
    public boolean inWater() {
        return getEntity().M();
    }

    @Override
    public boolean isWet() {
        return getEntity().L();
    }

    @Override
    public void setNoClipEnabled(boolean flag) {
        getEntity().X = flag;
    }

    @Override
    public void setTarget(LivingEntity livingEntity) {
        getEntity().setGoalTarget(((CraftLivingEntity) livingEntity).getHandle());
    }

    @Override
    public LivingEntity getTarget() {
        return (LivingEntity) getEntity().getGoalTarget().getBukkitEntity();
    }

    @Override
    public boolean attack(LivingEntity entity) {
        // TODO
        return false;
    }

    @Override
    public boolean attack(LivingEntity entity, float damage) {
        return attack(entity, DamageSource.mobAttack((EntityLiving) ((CraftEntity) entity).getHandle()), damage);
    }

    private boolean attack(LivingEntity entity, DamageSource damageSource, float damage) {
        PetAttackEvent attackEvent = new PetAttackEvent(this.getPet(), entity, damage);
        EchoPet.getCore().getServer().getPluginManager().callEvent(attackEvent);
        if (!attackEvent.isCancelled()) {
            if (entity instanceof EntityPlayer) {
                if (!(PetSettings.DAMAGE_PLAYERS.getValue())) {
                    return false;
                }
            }
            return getEntity().damageEntity(damageSource, (float) attackEvent.getDamage());
        }
        return false;
    }

    @Override
    public org.bukkit.entity.Entity getPassenger() {
        return getEntity().passenger == null ? null : getEntity().passenger.getBukkitEntity();
    }

    @Override
    public void setYaw(float yaw) {
        getEntity().lastYaw = getEntity().yaw = yaw;
    }

    @Override
    public void setStepHeight(float height) {
        getEntity().W = height;
    }

    @Override
    public void setPitch(float pitch) {
        getEntity().pitch = pitch;
    }

    @Override
    public void applyPitchAndYawChanges(float pitch, float yaw) {
        getEntity().lastYaw = getEntity().yaw = yaw;
        getEntity().pitch = pitch;
        getPet().getEntity().applyPitchAndYawChanges(getEntity().yaw, getEntity().pitch);
        getEntity().aO = getEntity().aM = getEntity().yaw;
    }

    @Override
    public float getPassengerSideMotion() {
        return ((EntityLiving) getPassenger()).bd;
    }

    @Override
    public float getPassengerForwardMotion() {
        return ((EntityLiving) getPassenger()).be;
    }

    @Override
    public void setSpeed(float value) {
        getEntity().i(value);
    }

    @Override
    public String getJumpField() {
        return "bc";
    }

    @Override
    public boolean isGrounded() {
        return getEntity().onGround;
    }

    @Override
    public double getLocX() {
        return getEntity().locX;
    }

    @Override
    public double getLocY() {
        return getEntity().locY;
    }

    @Override
    public double getLocZ() {
        return getEntity().locZ;
    }

    @Override
    public void setLocX(double value) {
        getEntity().locX = value;
    }

    @Override
    public void setLocY(double value) {
        getEntity().locY = value;
    }

    @Override
    public void setLocZ(double value) {
        getEntity().locZ = value;
    }

    @Override
    public float getLength() {
        return getEntity().length;
    }

    @Override
    public void jump() {
        getHandle().getControllerJump().a();
    }

    @Override
    public void lookAt(org.bukkit.entity.Entity entity, float headYaw) {
        getHandle().getControllerLook().a(((CraftEntity) entity).getHandle(), headYaw, NMSEntityUtil.getMaxHeadRotation(getEntity()));
    }

    @Override
    public void lookAt(org.bukkit.entity.Entity entity, float headYaw, float headPitch) {
        getHandle().getControllerLook().a(((CraftEntity) entity).getHandle(), headYaw, headPitch);
    }

    @Override
    public void lookAt(double x, double y, double z, float headYaw, float headPitch) {
        getHandle().getControllerLook().a(x, y, z, headYaw, headPitch);
    }

    @Override
    public void setLocation(org.bukkit.Location location) {
        getEntity().setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @Override
    public org.bukkit.entity.Entity findPlayer(double range) {
        Entity candidate = getEntity().world.findNearbyPlayer(getEntity(), range);
        return candidate != null ? candidate.getBukkitEntity() : null;
    }

    @Override
    public org.bukkit.entity.Entity findEntity(Class<?> nmsTypeClass, Object boundingBox) {
        return getEntity().world.a(nmsTypeClass, (AxisAlignedBB) boundingBox, getEntity()).getBukkitEntity();
    }

    @Override
    public Object growBoundingBox(double d0, double d1, double d2) {
        return getEntity().boundingBox.grow(d0, d1, d2);
    }

    @Override
    public float distanceTo(org.bukkit.entity.Entity entity) {
        return getEntity().e(((CraftEntity) entity).getHandle());
    }

    @Override
    public double getSpeed() {
        if (getEntity() == null) {
            return GenericAttributes.d.b();
        }
        return getEntity().getAttributeInstance(GenericAttributes.d).getValue();
    }

    @Override
    public void setSpeed(double speed) {
        getEntity().getAttributeInstance(GenericAttributes.d).setValue(speed);
    }

    @Override
    public float getPathfindingRadius() {
        return (float) getEntity().getAttributeInstance(GenericAttributes.b).getValue();
    }

    @Override
    public void setPathfindingRadius(float pathfindingRadius) {
        getEntity().getAttributeInstance(GenericAttributes.b).setValue(pathfindingRadius);
    }

    @Override
    public void navigateTo(org.bukkit.entity.Entity entity, double speed) {
        PathEntity path = getEntity().world.findPath(getEntity(), ((CraftEntity) entity).getHandle(), getPathfindingRadius(), true, false, false, true);
        navigateTo(path, speed);
    }

    @Override
    public void navigateTo(int x, int y, int z, double speed) {
        PathEntity path = getEntity().world.a(getEntity(), x, y, z, getPathfindingRadius(), true, false, false, true);
        navigateTo(path, speed);
    }

    private void navigateTo(PathEntity path, double speed) {
        getEntity().setPathEntity(path);
        getHandle().getNavigation().a(path, speed);
    }

    @Override
    public void stopNavigating() {
        getHandle().getNavigation().h();
    }

    @Override
    public boolean isNavigating() {
        return getHandle().getNavigation().g();
    }

    @Override
    public void setAvoidsWater(boolean flag) {
        getEntity().getNavigation().a(flag);
    }

    @Override
    public boolean getAvoidsWater() {
        return getEntity().getNavigation().a();
    }

    @Override
    public void setBreakDoors(boolean flag) {
        getEntity().getNavigation().b(flag);
    }

    @Override
    public boolean getBreakDoors() {
        return getEntity().getNavigation().c();
    }

    @Override
    public void setEnterDoors(boolean flag) {
        getEntity().getNavigation().c(flag);
    }

    @Override
    public void setAvoidSun(boolean flag) {
        getEntity().getNavigation().d(flag);
    }

    @Override
    public void setCanSwim(boolean flag) {
        getEntity().getNavigation().e(flag);
    }

    @Override
    public int getMaxHeadRotation() {
        return NMSEntityUtil.getMaxHeadRotation(getEntity());
    }

    @Override
    public void setInvisible(boolean flag) {
        getEntity().setInvisible(flag);
    }

    @Override
    public boolean isInvisible() {
        return getEntity().isInvisible();
    }

    @Override
    public void setSneaking(boolean flag) {
        getEntity().setSneaking(flag);
    }

    @Override
    public boolean isSneaking() {
        return getEntity().isSneaking();
    }

    @Override
    public void setSprinting(boolean flag) {
        getEntity().setSprinting(flag);
    }

    @Override
    public boolean isSprinting() {
        return getEntity().isSprinting();
    }
}