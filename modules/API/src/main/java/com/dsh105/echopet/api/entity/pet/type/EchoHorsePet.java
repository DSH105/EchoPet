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
import com.dsh105.echopet.api.entity.attribute.Attributes;
import com.dsh105.echopet.api.entity.entitypet.type.EntityHorsePet;
import com.dsh105.echopet.api.entity.pet.EchoAgeablePet;
import com.dsh105.echopet.bridge.entity.type.HorseEntityBridge;

import java.util.UUID;

public class EchoHorsePet extends EchoAgeablePet<HorseEntityBridge, EntityHorsePet> implements HorsePet {

    private int rearingCounter;

    public EchoHorsePet(UUID playerUID) {
        super(playerUID);
        getEntity().setTame(false);
    }

    @Override
    public void setVariant(Attributes.HorseVariant variant) {
        getEntity().setHorseVariant(variant);
    }

    @Override
    public Attributes.HorseVariant getVariant() {
        return getEntity().getHorseVariant();
    }

    @Override
    public void setColor(Attributes.HorseColor color) {
        getEntity().setColor(color);
    }

    @Override
    public Attributes.HorseColor getColor() {
        return getEntity().getColor();
    }

    @Override
    public void setStyle(Attributes.HorseStyle style) {
        getEntity().setStyle(style);
    }

    @Override
    public Attributes.HorseStyle getStyle() {
        return getEntity().getStyle();
    }

    @Override
    public void setArmour(Attributes.HorseArmour armour) {
        getEntity().setArmour(armour);
    }

    @Override
    public Attributes.HorseArmour getArmour() {
        return getEntity().getArmour();
    }

    @Override
    public void setSaddled(boolean flag) {
        getEntity().setSaddled(flag);
    }

    @Override
    public boolean isSaddled() {
        return getEntity().isSaddled();
    }

    @Override
    public void setChested(boolean flag) {
        getEntity().setChested(flag);
    }

    @Override
    public boolean isChested() {
        return getEntity().isChested();
    }

    @Override
    public SizeCategory getSizeCategory() {
        return isBaby() ? SizeCategory.TINY : SizeCategory.LARGE;
    }

    @Override
    public void onLive() {
        super.onLive();
        if (rearingCounter > 0 && ++rearingCounter > 20) {
            getEntity().animation(EntityHorsePet.ANIMATION_REAR, false);
        }
    }

    @Override
    public void doJumpAnimation() {
        getEntity().makeSound("mob.horse.jump", 0.4F, 1.0F);
        this.rearingCounter = 1;
        getEntity().animation(EntityHorsePet.ANIMATION_REAR, true);
    }
}