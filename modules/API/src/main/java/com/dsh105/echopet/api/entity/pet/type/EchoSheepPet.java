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

import com.dsh105.echopet.api.entity.attribute.Attributes;
import com.dsh105.echopet.api.entity.entitypet.type.EntitySheepPet;
import com.dsh105.echopet.api.entity.pet.EchoAgeablePet;
import com.dsh105.echopet.bridge.entity.type.SheepEntityBridge;

import java.util.UUID;

public class EchoSheepPet extends EchoAgeablePet<SheepEntityBridge, EntitySheepPet> implements SheepPet {

    public EchoSheepPet(UUID playerUID) {
        super(playerUID);
    }

    @Override
    public void setSheared(boolean flag) {
        getEntity().setSheared(flag);
    }

    @Override
    public boolean isSheared() {
        return getEntity().isSheared();
    }

    @Override
    public Attributes.Color getColor() {
        return getEntity().getDyeColor();
    }

    @Override
    public void setColor(Attributes.Color dyeColor) {
        getEntity().setDyeColor(dyeColor);
    }
}