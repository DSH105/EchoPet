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

import com.dsh105.echopet.api.entity.*;
import com.dsh105.echopet.api.entity.entitypet.type.EntityIronGolemPet;
import com.dsh105.echopet.api.entity.pet.PetBase;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;

@PetInfo(type = PetType.IRON_GOLEM, width = 1.4F, height = 2.9F)
public class EchoIronGolemPet extends PetBase<IronGolem, EntityIronGolemPet> implements IronGolemPet {

    private boolean holdingRose;
    private int holdingRoseCounter;

    public EchoIronGolemPet(Player owner) {
        super(owner);
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.LARGE;
    }

    @Override
    public String getIdleSound() {
        return "";
    }

    @Override
    public String getDeathSound() {
        return "mob.irongolem.death";
    }

    @Override
    public void makeStepSound() {
        getEntity().makeSound("mob.irongolem.walk", 1.0F, 1.0F);
    }

    @AttributeHandler(data = PetData.ROSE)
    @Override
    public void setHoldingRose(boolean flag) {
        getEntity().setHoldingRose(flag);
        this.holdingRose = flag;
        this.holdingRoseCounter = 400;
    }

    @AttributeHandler(data = PetData.ROSE, getter = true)
    @Override
    public boolean getHoldingRose() {
        return holdingRose;
    }

    @Override
    public void onLive() {
        if (this.holdingRoseCounter > 0 && --this.holdingRoseCounter <= 0) {
            if (getHoldingRose()) {
                setHoldingRose(true);
            } else {
                this.holdingRoseCounter = 0;
            }
        }
    }
}