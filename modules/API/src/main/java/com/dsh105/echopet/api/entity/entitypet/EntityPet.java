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

import com.dsh105.echopet.api.entity.pet.Pet;

public interface EntityPet<T extends Pet> {

    T getPet();

    EntityPetModifier<T> getModifier();

    void checkDespawn();

    void incrementAge();

    Object getEntitySenses();

    Object getNavigation();

    void mobTick();

    Object getControllerMove();

    Object getControllerJump();

    Object getControllerLook();

    void applyPitchAndYawChanges(float f, float f1);

    void updateMotion(float sideMotion, float forwardMotion);

    void modifyBoundingBox(float f, float f1);

    void setFireProof(boolean flag);

    boolean isFireProof();

    boolean isInvulnerable();

    float getSoundVolume();

    void makeSound(String s, float f, float f1);
}