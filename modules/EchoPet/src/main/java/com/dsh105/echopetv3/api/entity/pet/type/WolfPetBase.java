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

package com.dsh105.echopetv3.api.entity.pet.type;

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.echopetv3.api.entity.AttributeHandler;
import com.dsh105.echopetv3.api.entity.PetData;
import com.dsh105.echopetv3.api.entity.PetInfo;
import com.dsh105.echopetv3.api.entity.PetType;
import com.dsh105.echopetv3.api.entity.entitypet.type.EntityWolfPet;
import com.dsh105.echopetv3.api.entity.pet.AgeablePetBase;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

@PetInfo(type = PetType.WOLF, width = 0.6F, height = 0.8F)
public class WolfPetBase extends AgeablePetBase<Wolf, EntityWolfPet> implements WolfPet {

    private boolean wet;
    private boolean shaking;
    private float shakeCount;

    public WolfPetBase(Player owner) {
        super(owner);
    }

    @AttributeHandler(dataType = PetData.Type.COLOUR)
    @Override
    public void setCollarColor(DyeColor dyeColor) {

    }

    @AttributeHandler(dataType = PetData.Type.COLOUR, getter = true)
    @Override
    public DyeColor getCollarColor() {
        return null;
    }

    @AttributeHandler(data = PetData.TAMED)
    @Override
    public void setTamed(boolean flag) {

    }

    @AttributeHandler(data = PetData.TAMED, getter = true)
    @Override
    public boolean isTamed() {
        return false;
    }

    @AttributeHandler(data = PetData.ANGRY)
    @Override
    public void setAngry(boolean flag) {

    }

    @AttributeHandler(data = PetData.ANGRY, getter = true)
    @Override
    public boolean isAngry() {
        return false;
    }

    @Override
    public String getIdleSound() {
        return isAngry() ? "mob.wolf.growl" : (GeneralUtil.random().nextInt(3) == 0 ? (this.isTamed() && getEntity().getTailHealth() < 10 ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
    }

    @Override
    public String getDeathSound() {
        return "mob.wolf.death";
    }

    @Override
    public void onLive() {
        super.onLive();
        if (getEntity().isWet()) {
            this.wet = true;
            this.shaking = false;
            this.shakeCount = 0.0F;
        } else if ((this.wet || this.shaking) && this.shaking) {
            if (this.shakeCount == 0.0F) {
                getEntity().makeSound("mob.wolf.shake", this.isBaby() ? (GeneralUtil.random().nextFloat() - GeneralUtil.random().nextFloat()) * 0.2F + 1.5F : (GeneralUtil.random().nextFloat() - GeneralUtil.random().nextFloat()) * 0.2F + 1.0F, (GeneralUtil.random().nextFloat() - GeneralUtil.random().nextFloat()) * 0.2F + 1.0F);
            }

            this.shakeCount += 0.05F;
            if (this.shakeCount - 0.05F >= 2.0F) {
                this.wet = false;
                this.shaking = false;
                this.shakeCount = 0.0F;
            }

            if (this.shakeCount > 0.4F) {
                getEntity().shakeParticle(shakeCount);
            }
        }
    }
}