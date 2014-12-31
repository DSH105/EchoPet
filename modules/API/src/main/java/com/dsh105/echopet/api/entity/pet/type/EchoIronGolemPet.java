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

import com.dsh105.echopet.api.entity.SizeCategory;
import com.dsh105.echopet.api.entity.entitypet.type.EntityIronGolemPet;
import com.dsh105.echopet.bridge.entity.type.IronGolemEntityBridge;
import com.dsh105.echopet.api.entity.pet.AbstractPetBase;

import java.util.UUID;

public class EchoIronGolemPet extends AbstractPetBase<IronGolemEntityBridge, EntityIronGolemPet> implements IronGolemPet {

    private boolean holdingRose;
    private int holdingRoseCounter;

    public EchoIronGolemPet(UUID playerUID) {
        super(playerUID);
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.LARGE;
    }

    @Override
    public void setHoldingRose(boolean flag) {
        getEntity().setHoldingRose(flag);
        this.holdingRose = flag;
        this.holdingRoseCounter = 400;
    }

    @Override
    public boolean getHoldingRose() {
        return holdingRose;
    }

    @Override
    public void onLive() {
        if (this.holdingRoseCounter > 0 && --this.holdingRoseCounter <= 0) {
            if (holdingRose) {
                setHoldingRose(true);
            } else {
                this.holdingRoseCounter = 0;
            }
        }
    }
}