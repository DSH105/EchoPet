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

import com.dsh105.echopet.api.entity.entitypet.type.EntityBatPet;
import com.dsh105.echopet.bridge.entity.type.BatEntityBridge;
import com.dsh105.echopet.api.entity.pet.AbstractPetBase;

import java.util.UUID;

public class EchoBatPet extends AbstractPetBase<BatEntityBridge, EntityBatPet> implements BatPet {

    public EchoBatPet(UUID playerUID) {
        super(playerUID);
        getEntity().setAsleep(false);
    }

    @Override
    public void setStationary(boolean flag) {
        super.setStationary(flag);
        getEntity().setAsleep(flag);
    }

    @Override
    public void setAsleep(boolean flag) {
        getEntity().setAsleep(flag);
    }

    @Override
    public boolean isAsleep() {
        return getEntity().isAsleep();
    }

    @Override
    public void onLive() {
        super.onLive();
        /*if (isAsleep()) {
            getBridgeEntity().setVelocity(new Vector(0D, 0D, 0D));
            getModifier().setLocY(Math.floor(getModifier().getLocY()) + 1.0D - (double) getModifier().getLength());
        } else {
            getModifier().setMotionY(getModifier().getMotionY() * 0.6000000238418579D);
        }*/
    }
}