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
        return pet.getEntity();
    }

    public EchoEntityPetHandle getHandle() {
        return (EchoEntityPetHandle) entity;
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
        if (pet.getPetGoalSelector() == null) {
            this.getBukkitEntity().remove();
            return;
        }
        pet.getPetGoalSelector().updateGoals();

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
        return entity.getBukkitEntity();
    }

    @Override
    public T getPet() {
        return pet;
    }

    @Override
    public void setMotionX(double motX) {
        entity.motX = motX;
    }

    @Override
    public void setMotionY(double motY) {
        entity.motY = motY;
    }

    @Override
    public void setMotionZ(double motZ) {
        entity.motZ = motZ;
    }

    @Override
    public double getMotionX() {
        return entity.motX;
    }

    @Override
    public double getMotionY() {
        return entity.motY;
    }

    @Override
    public double getMotionZ() {
        return entity.motZ;
    }

    @Override
    public boolean canSee(org.bukkit.entity.Entity entity) {
        return getHandle().getEntitySenses().canSee(((CraftEntity) entity).getHandle());
    }

    @Override
    public boolean isDead() {
        return entity.dead;
    }

    @Override
    public boolean inLava() {
        return entity.P();
    }

    @Override
    public boolean inWater() {
        return entity.M();
    }

    @Override
    public boolean isWet() {
        return entity.L();
    }

    @Override
    public void setNoClipEnabled(boolean flag) {
        entity.X = flag;
    }

    @Override
    public void setTarget(LivingEntity livingEntity) {
        entity.setGoalTarget(((CraftLivingEntity) livingEntity).getHandle());
    }

    @Override
    public LivingEntity getTarget() {
        return (LivingEntity) entity.getGoalTarget().getBukkitEntity();
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
        PetAttackEvent attackEvent = new PetAttackEvent(pet, entity, damage);
        EchoPet.getCore().getServer().getPluginManager().callEvent(attackEvent);
        if (!attackEvent.isCancelled()) {
            if (entity instanceof EntityPlayer) {
                if (!(PetSettings.DAMAGE_PLAYERS.getValue())) {
                    return false;
                }
            }
            return this.entity.damageEntity(damageSource, (float) attackEvent.getDamage());
        }
        return false;
    }

    @Override
    public org.bukkit.entity.Entity getPassenger() {
        return entity.passenger == null ? null : entity.passenger.getBukkitEntity();
    }

    @Override
    public void setYaw(float yaw) {
        entity.lastYaw = entity.yaw = yaw;
    }

    @Override
    public void setStepHeight(float height) {
        entity.W = height;
    }

    @Override
    public void setPitch(float pitch) {
        entity.pitch = pitch;
    }

    @Override
    public void applyPitchAndYawChanges(float pitch, float yaw) {
        entity.lastYaw = entity.yaw = yaw;
        entity.pitch = pitch;
        pet.getEntity().applyPitchAndYawChanges(entity.yaw, entity.pitch);
        entity.aO = entity.aM = entity.yaw;
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
        entity.i(value);
    }

    @Override
    public String getJumpField() {
        return "bc";
    }

    @Override
    public boolean isGrounded() {
        return entity.onGround;
    }

    @Override
    public double getLocX() {
        return entity.locX;
    }

    @Override
    public double getLocY() {
        return entity.locY;
    }

    @Override
    public double getLocZ() {
        return entity.locZ;
    }

    @Override
    public void setLocX(double value) {
        entity.locX = value;
    }

    @Override
    public void setLocY(double value) {
        entity.locY = value;
    }

    @Override
    public void setLocZ(double value) {
        entity.locZ = value;
    }

    @Override
    public float getLength() {
        return entity.length;
    }

    @Override
    public void jump() {
        getHandle().getControllerJump().a();
    }

    @Override
    public void lookAt(org.bukkit.entity.Entity entity, float headYaw) {
        getHandle().getControllerLook().a(((CraftEntity) entity).getHandle(), headYaw, NMSEntityUtil.getMaxHeadRotation(this.entity));
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
        entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @Override
    public org.bukkit.entity.Entity findPlayer(double range) {
        Entity candidate = entity.world.findNearbyPlayer(entity, range);
        return candidate != null ? candidate.getBukkitEntity() : null;
    }

    @Override
    public org.bukkit.entity.Entity findEntity(Class<?> nmsTypeClass, Object boundingBox) {
        return entity.world.a(nmsTypeClass, (AxisAlignedBB) boundingBox, entity).getBukkitEntity();
    }

    @Override
    public Object growBoundingBox(double d0, double d1, double d2) {
        return entity.boundingBox.grow(d0, d1, d2);
    }

    @Override
    public float distanceTo(org.bukkit.entity.Entity entity) {
        return this.entity.e(((CraftEntity) entity).getHandle());
    }

    @Override
    public double getSpeed() {
        if (entity == null) {
            return GenericAttributes.d.b();
        }
        return entity.getAttributeInstance(GenericAttributes.d).getValue();
    }

    @Override
    public void setSpeed(double speed) {
        entity.getAttributeInstance(GenericAttributes.d).setValue(speed);
    }

    @Override
    public float getPathfindingRadius() {
        return (float) entity.getAttributeInstance(GenericAttributes.b).getValue();
    }

    @Override
    public void setPathfindingRadius(float pathfindingRadius) {
        entity.getAttributeInstance(GenericAttributes.b).setValue(pathfindingRadius);
    }

    @Override
    public void navigateTo(org.bukkit.entity.Entity entity, double speed) {
        PathEntity path = this.entity.world.findPath(this.entity, ((CraftEntity) entity).getHandle(), getPathfindingRadius(), true, false, false, true);
        navigateTo(path, speed);
    }

    @Override
    public void navigateTo(int x, int y, int z, double speed) {
        PathEntity path = entity.world.a(entity, x, y, z, getPathfindingRadius(), true, false, false, true);
        navigateTo(path, speed);
    }

    private void navigateTo(PathEntity path, double speed) {
        entity.setPathEntity(path);
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
        entity.getNavigation().a(flag);
    }

    @Override
    public boolean getAvoidsWater() {
        return entity.getNavigation().a();
    }

    @Override
    public void setBreakDoors(boolean flag) {
        entity.getNavigation().b(flag);
    }

    @Override
    public boolean getBreakDoors() {
        return entity.getNavigation().c();
    }

    @Override
    public void setEnterDoors(boolean flag) {
        entity.getNavigation().c(flag);
    }

    @Override
    public void setAvoidSun(boolean flag) {
        entity.getNavigation().d(flag);
    }

    @Override
    public void setCanSwim(boolean flag) {
        entity.getNavigation().e(flag);
    }

    @Override
    public int getMaxHeadRotation() {
        return NMSEntityUtil.getMaxHeadRotation(entity);
    }

    @Override
    public void setInvisible(boolean flag) {
        entity.setInvisible(flag);
    }

    @Override
    public boolean isInvisible() {
        return entity.isInvisible();
    }

    @Override
    public void setSneaking(boolean flag) {
        entity.setSneaking(flag);
    }

    @Override
    public boolean isSneaking() {
        return entity.isSneaking();
    }

    @Override
    public void setSprinting(boolean flag) {
        entity.setSprinting(flag);
    }

    @Override
    public boolean isSprinting() {
        return entity.isSprinting();
    }
}