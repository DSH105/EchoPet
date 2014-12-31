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

import com.dsh105.commodus.container.PositionContainer;
import com.dsh105.echopet.api.entity.pet.Pet;

public interface EntityPetModifier<T extends Pet> {

    T getPet();

    // So it won't despawn if there are no nearby players
    boolean isPersistent();

    // Whether to use the new NMS AI or not
    boolean useNewAi();

    // 'on tick' function
    void onTick();

    Object getBukkitEntity();

    void setMotionX(double motX);

    void setMotionY(double motY);

    void setMotionZ(double motZ);

    double getMotionX();

    double getMotionY();

    double getMotionZ();

    boolean canSee(Object entity);

    boolean isDead();

    boolean inLava();

    boolean inWater();

    boolean isWet();

    public void setNoClipEnabled(boolean flag);

    void setTarget(Object livingEntity);

    Object getTarget();

    boolean attack(Object livingEntity);

    boolean attack(Object livingEntity, float damage);

    Object getPassenger();

    void setYaw(float yaw); // set last yaw too

    void setStepHeight(float height);

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

    void lookAt(Object entity, float headYaw);

    void lookAt(Object entity, float headYaw, float headPitch);

    void lookAt(double x, double y, double z, float headYaw, float headPitch);

    void setLocation(PositionContainer position);

    Object findPlayer(double range);

    Object findEntity(Class<?> nmsTypeClass, Object boundingBox);

    Object growBoundingBox(double d0, double d1, double d2);

    float distanceTo(Object entity);

    double getSpeed();

    void setSpeed(double speed);

    float getPathfindingRadius();

    void setPathfindingRadius(float pathfindingRadius);

    void navigateTo(Object entity, double speed);

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