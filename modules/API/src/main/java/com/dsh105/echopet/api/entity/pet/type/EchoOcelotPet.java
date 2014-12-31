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
import com.dsh105.echopet.api.entity.entitypet.type.EntityOcelotPet;
import com.dsh105.echopet.api.entity.pet.EchoAgeablePet;
import com.dsh105.echopet.bridge.entity.type.OcelotEntityBridge;

import java.util.UUID;

public class EchoOcelotPet extends EchoAgeablePet<OcelotEntityBridge, EntityOcelotPet> implements OcelotPet {

    public EchoOcelotPet(UUID playerUID) {
        super(playerUID);
    }

    @Override
    public Attributes.OcelotType getCatType() {
        return getEntity().getBukkitCatType();
    }

    @Override
    public void setCatType(Attributes.OcelotType type) {
        getEntity().setBukkitCatType(type);
    }

    @Override
    public void setStationary(boolean flag) {
        super.setStationary(flag);
        getBridgeEntity().setSitting(flag);
    }
}