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

package com.dsh105.echopet.api.entity.pet.type;

import com.captainbern.minecraft.wrapper.WrappedDataWatcher;
import com.dsh105.echopet.api.entity.*;
import com.dsh105.echopet.api.entity.entitypet.type.EntityHumanPet;
import com.dsh105.echopet.api.entity.pet.EquipablePet;
import com.dsh105.echopet.api.entity.pet.Hostility;
import com.dsh105.echopet.bridge.entity.type.HumanEntityBridge;
import com.dsh105.echopet.util.WrappedGameProfile;

import java.util.UUID;

@Traits(type = PetType.HUMAN, hositility = Hostility.NEUTRAL, width = 0.6F, height = 1.8F, health = 20.0D, attackDamage = 6.0D)
@Voice(idle = "random.breathe", death = "random.classic_hurt", step = "step.grass")
@Size(SizeCategory.REGULAR)
public interface HumanPet extends EquipablePet<HumanEntityBridge, EntityHumanPet> {

    WrappedDataWatcher getDataWatcher();

    byte getEntityStatus();

    boolean isInitiated();

    int getId();

    UUID getProfileUuid();

    WrappedGameProfile getGameProfile();

    void updatePosition();

    void updateDataWatcher();
}