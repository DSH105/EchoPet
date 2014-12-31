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

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.echopet.api.entity.attribute.Attributes;
import com.dsh105.echopet.api.entity.entitypet.type.EntityWolfPet;
import com.dsh105.echopet.api.entity.pet.EchoAgeablePet;
import com.dsh105.echopet.bridge.entity.type.WolfEntityBridge;

import java.util.UUID;

public class EchoWolfPet extends EchoAgeablePet<WolfEntityBridge, EntityWolfPet> implements WolfPet {

    private boolean wet;
    private boolean shaking;
    private float shakeCount;

    public EchoWolfPet(UUID playerUID) {
        super(playerUID);
    }

    @Override
    public void setCollarColor(Attributes.Color color) {
        getEntity().setWolfCollarColor(color);
    }

    @Override
    public Attributes.Color getCollarColor() {
        return getEntity().getWolfCollarColor();
    }

    @Override
    public void setTamed(boolean flag) {
        if (flag && isAngry()) {
            setAttribute(Attributes.Attribute.ANGRY, false);
        }
        getEntity().setTamed(flag);
    }

    @Override
    public boolean isTamed() {
        return getEntity().isTamed();
    }

    @Override
    public void setAngry(boolean flag) {
        if (flag && isTamed()) {
            setAttribute(Attributes.Attribute.TAME, false);
        }
        getEntity().setAngry(flag);
    }

    @Override
    public boolean isAngry() {
        return getEntity().isAngry();
    }

    @Override
    public String getIdleSound() {
        String idleSound = super.getIdleSound();
        return idleSound.equals("default") || idleSound.isEmpty() ? idleSound : String.format(super.getIdleSound(), isAngry() ? "growl" : (GeneralUtil.random().nextInt(3) == 0 ? (this.isTamed() && getEntity().getTailHealth() < (getBridgeEntity().getMaxHealth() / 2) ? "whine" : "panting") : "bark"));
    }

    @Override
    public void setStationary(boolean flag) {
        super.setStationary(flag);
        getBridgeEntity().setSitting(flag);
    }

    @Override
    public void onLive() {
        super.onLive();
        /*if (getModifier().isWet()) {
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
        }*/
    }
}