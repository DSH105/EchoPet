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

public interface EntityPet<T extends Pet> {

    T getPet();

    void onLive();

    Entity getBukkitEntity();

    boolean isInvisible();

    void setInvisible(boolean flag);

    boolean isSneaking();

    void setSneaking(boolean flag);

    boolean isSprinting();

    void setSprinting(boolean flag);

    void setMotionX(double motX);

    void setMotionY(double motY);

    void setMotionZ(double motZ);

    double getMotionX();

    double getMotionY();

    double getMotionZ();

    PetGoalSelector getGoalSelector();

    void setGoalSelector(PetGoalSelector goalSelector);

    boolean canSee(Entity entity);

    boolean isDead();

    boolean inLava();

    boolean inWater();

    boolean isWet();

    public void setNoClipEnabled(boolean flag);

    void setTarget(LivingEntity livingEntity);

    LivingEntity getTarget();

    boolean attack(LivingEntity entity);

    void setPositionRotation(double posX, double posY, double posZ, float yaw, float pitch);

    boolean attack(LivingEntity entity, float damage);

    Entity getPassenger();

    void setFireProof(boolean flag);

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

    void makeSound(String soundName, float f1, float f2);

    float getSoundVolume();

    void jump();

    void lookAt(Entity entity, float headYaw);

    void lookAt(Entity entity, float headYaw, float headPitch);

    void lookAt(double x, double y, double z, float headYaw, float headPitch);

    void setLocation(Location location);

    void modifyBoundingBox(float width, float height);

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

    int getMaxHeadRotation();

    void updateMotion(float sideMotion, float forwardMotion);

    boolean onInteract(org.bukkit.entity.Player player);
}