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

package com.dsh105.echopet.api.entity.pet;

import com.dsh105.echopet.api.entity.PetData;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.SizeCategory;
import com.dsh105.echopet.api.entity.ai.PetGoalSelector;
import com.dsh105.echopet.api.entity.entitypet.EntityPet;
import com.dsh105.echopet.api.entity.entitypet.EntityPetModifier;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface Pet<T extends LivingEntity, S extends EntityPet> {

    T getBukkitEntity();

    S getEntity();

    <P extends Pet<T, S>> EntityPetModifier<P> getModifier();

    UUID getPetId();

    String getName();

    PetGoalSelector getPetGoalSelector();

    boolean setName(String name);

    boolean setName(String name, boolean sendFailMessage);

    String getOwnerIdent();

    Player getOwner();

    String getOwnerName();

    PetType getType();

    boolean isRider();

    Pet getRider();

    float width();

    float height();

    SizeCategory getSizeCategory();

    String getIdleSound();

    String getHurtSound();

    String getDeathSound();

    void makeStepSound();

    void setDataValue(PetData petData, Object value);

    List<PetData> getRegisteredData();

    List<PetData> getActiveDataValues();

    void setDataValue(PetData... dataArray);

    void setDataValue(boolean on, PetData... dataArray);

    void invertDataValue(PetData petData);

    boolean isStationary();

    void setStationary(boolean flag);

    void despawn(boolean makeDeathSound);

    Pet spawnRider(final PetType type, boolean sendFailMessage);

    Pet spawnRider(Pet pet, boolean sendFailMessage);

    void despawnRider();

    boolean teleportToOwner();

    boolean teleport(Location to);

    Location getLocation();

    double getJumpHeight();

    void setJumpHeight(double jumpHeight);

    double getRideSpeed();

    void setRideSpeed(double rideSpeed);

    boolean shouldVanish();

    void setShouldVanish(boolean flag);

    boolean isOwnerRiding();

    boolean isOwnerInMountingProcess();

    void setOwnerRiding(boolean flag);

    void setHat(boolean flag);

    boolean isHat();

    void onError(Throwable e);

    void onLive();

    void onRide(float sideMotion, float forwardMotion);

    boolean onInteract(Player player);

    void doJumpAnimation();
}