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

import com.dsh105.commodus.container.PositionContainer;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.SizeCategory;
import com.dsh105.echopet.api.entity.Voice;
import com.dsh105.echopet.api.entity.ai.Mind;
import com.dsh105.echopet.api.entity.attribute.AttributeType;
import com.dsh105.echopet.api.entity.attribute.Attributes;
import com.dsh105.echopet.api.entity.attribute.EntityAttribute;
import com.dsh105.echopet.api.entity.entitypet.EntityPet;
import com.dsh105.echopet.api.entity.entitypet.EntityPetModifier;
import com.dsh105.echopet.bridge.PlayerBridge;
import com.dsh105.echopet.bridge.entity.LivingEntityBridge;

import java.util.List;
import java.util.UUID;

public interface Pet<T extends LivingEntityBridge, S extends EntityPet> {

    T getBridgeEntity();

    S getEntity();

    <P extends Pet<T, S>> EntityPetModifier<P> getModifier();

    UUID getPetId();

    String getName();

    Mind getMind();

    boolean setName(String name);

    boolean setName(String name, boolean sendFailMessage);

    UUID getOwnerUID();

    String getOwnerUIDAsString();

    PlayerBridge getOwner();

    String getOwnerName();

    PetType getType();

    boolean isRider();

    Pet getRider();

    float width();

    float height();

    SizeCategory getSizeCategory();

    Voice getVoice();

    String getIdleSound();

    String getHurtSound();

    String getDeathSound();

    void makeStepSound();

    boolean getAttribute(Attributes.Attribute attribute);

    void setAttribute(Attributes.Attribute attribute, boolean value);

    void invertAttribute(Attributes.Attribute attribute);

    EntityAttribute getAttribute(AttributeType attributeType);

    void setAttribute(EntityAttribute entityAttribute);

    List<EntityAttribute> getValidAttributes();

    List<EntityAttribute> getActiveAttributes();

    boolean isStationary();

    void setStationary(boolean flag);

    void remove(boolean makeDeathSound);

    Pet spawnRider(final PetType type, boolean sendFailMessage);

    Pet setRider(Pet pet, boolean sendFailMessage);

    void removeRider();

    boolean moveToOwner();

    boolean move(PositionContainer to);

    PositionContainer getLocation();

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

    boolean onInteract(PlayerBridge player);

    void doJumpAnimation();
}