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

import com.dsh105.echopet.api.entity.entitypet.type.EntityEndermanPet;
import com.dsh105.echopet.bridge.entity.type.EndermanEntityBridge;
import com.dsh105.echopet.api.entity.pet.AbstractPetBase;

import java.util.UUID;

public class EchoEndermanPet extends AbstractPetBase<EndermanEntityBridge, EntityEndermanPet> implements EndermanPet {

    private boolean screaming;
    private int timer = 50;

    public EchoEndermanPet(UUID playerUID) {
        super(playerUID);
    }


    @Override
    public void setScreaming(boolean flag) {
        getEntity().setScreaming(flag);
        // Keep track of it so that the enderman doesn't stop screaming
        this.screaming = flag;
    }

    @Override
    public boolean isScreaming() {
        return getEntity().isScreaming();
    }

    @Override
    public void onLive() {
        if (this.screaming && --timer >= 0) {
            timer = 50;
            this.setScreaming(true);
        }
    }
}