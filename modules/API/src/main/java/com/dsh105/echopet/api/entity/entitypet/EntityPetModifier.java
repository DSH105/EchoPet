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

package com.dsh105.echopet.api.entity.entitypet;

import com.dsh105.echopet.api.entity.ai.PetGoalSelector;
import com.dsh105.echopet.api.entity.pet.Pet;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public interface EntityPetModifier<T extends Pet> {

    T getPet();

    // So it won't despawn if there are no nearby players
    boolean isPersistent();

    // Whether to use the new NMS AI or not
    boolean useNewAi();

    // 'on tick' function
    void onTick();

    Entity getBukkitEntity();

    void setMotionX(double motX);

    void setMotionY(double motY);

    void setMotionZ(double motZ);

    double getMotionX();

    double getMotionY();

    double getMotionZ();

    boolean canSee(Entity entity);

    boolean isDead();

    boolean inLava();

    boolean inWater();

    boolean isWet();

    public void setNoClipEnabled(boolean flag);

    void setTarget(LivingEntity livingEntity);

    LivingEntity getTarget();

    boolean attack(LivingEntity entity);

    boolean attack(LivingEntity entity, float damage);

    Entity getPassenger();

    void setYaw(float yaw); // set last yaw too

    void setBlockClimbHeight(float height);

    void setPitch(float pitch);

    void applyPitchAndYawChanges(float pitch, float yaw);

    float getPassengerSideMotion();

    float getPassengerForwardMotion();

    void setSpeed(float value);

    String getJumpField();

    boolean isGrounded();

    double getLocX();

    double getLocY();

    double getLocZ();

    void setLocX(double value);

    void setLocY(double value);

    void setLocZ(double value);

    float getLength();

    void jump();

    void lookAt(Entity entity, float headYaw);

    void lookAt(Entity entity, float headYaw, float headPitch);

    void lookAt(double x, double y, double z, float headYaw, float headPitch);

    void setLocation(Location location);

    Entity findPlayer(double range);

    Entity findEntity(Class<?> nmsTypeClass, Object boundingBox);

    Object growBoundingBox(double d0, double d1, double d2);

    float distanceTo(Entity entity);

    float getPathfindingRadius();

    void setPathfindingRadius(float pathfindingRadius);

    void navigateTo(Entity entity, double speed);

    void navigateTo(int x, int y, int z, double speed);

    void stopNavigating();

    boolean isNavigating();

    void setAvoidsWater(boolean flag);

    boolean getAvoidsWater();

    void setBreakDoors(boolean flag);

    boolean getBreakDoors();

    void setEnterDoors(boolean flag);

    void setAvoidSun(boolean flag);

    void setCanSwim(boolean flag);

    int getMaxHeadRotation();

    void setInvisible(boolean flag);

    boolean isInvisible();

    void setSneaking(boolean flag);

    boolean isSneaking();

    void setSprinting(boolean flag);

    boolean isSprinting();
}