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

import com.dsh105.echopet.api.entity.entitypet.type.EntityWitherPet;
import com.dsh105.echopet.api.entity.pet.EchoRangedPet;
import com.dsh105.echopet.bridge.entity.type.WitherEntityBridge;

import java.util.UUID;

public class EchoWitherPet extends EchoRangedPet<WitherEntityBridge, EntityWitherPet> implements WitherPet {

    public EchoWitherPet(UUID playerUID) {
        super(playerUID);
    }

    @Override
    public void setShielded(boolean flag) {
        getEntity().setShielded(flag);
    }

    @Override
    public boolean isShielded() {
        return getEntity().isShielded();
    }

    @Override
    public String getIdleSound() {
        return "mob.wither.idle";
    }

    @Override
    public String getDeathSound() {
        return "mob.wither.death";
    }

    @Override
    public void onLive() {
        super.onLive();
        if (isShielded()) {
            // Keep it constant - wither health regenerates
            getBridgeEntity().setHealth(150F);
        } else if (getBridgeEntity().getHealth() <= getBridgeEntity().getMaxHealth()) {
            getBridgeEntity().setHealth(getBridgeEntity().getMaxHealth());
        }
    }
}