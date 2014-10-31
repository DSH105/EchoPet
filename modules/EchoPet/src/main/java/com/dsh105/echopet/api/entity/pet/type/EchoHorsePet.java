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
import com.dsh105.echopet.api.entity.entitypet.type.EntityHorsePet;
import com.dsh105.echopet.api.entity.pet.EchoAgeablePet;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

@PetInfo(type = PetType.HORSE, width = 1.4F, height = 1.6F)
public class EchoHorsePet extends EchoAgeablePet<Horse, EntityHorsePet> implements HorsePet {

    private int rearingCounter;

    public EchoHorsePet(Player owner) {
        super(owner);
        getEntity().setTame(false);
    }

    @AttributeHandler(dataType = PetData.Type.VARIANT)
    @Override
    public void setVariant(Horse.Variant type) {
        getEntity().setHorseVariant(type);
    }

    @AttributeHandler(dataType = PetData.Type.HORSE_COLOUR)
    @Override
    public void setColor(Horse.Color variant) {
        getEntity().setColor(variant);
    }

    @AttributeHandler(dataType = PetData.Type.HORSE_STYLE)
    @Override
    public void setStyle(Horse.Style style) {
        getEntity().setStyle(style);
    }

    @AttributeHandler(dataType = PetData.Type.HORSE_ARMOUR)
    @Override
    public void setArmour(Armour armour) {
        getEntity().setArmour(armour);
    }

    @AttributeHandler(data = PetData.SADDLE)
    @Override
    public void setSaddled(boolean flag) {
        getEntity().setSaddled(flag);
    }

    @AttributeHandler(data = PetData.CHESTED)
    @Override
    public void setChested(boolean flag) {
        getEntity().setChested(flag);
    }

    @AttributeHandler(dataType = PetData.Type.VARIANT)
    @Override
    public Horse.Variant getVariant() {
        return getEntity().getHorseVariant();
    }

    @AttributeHandler(dataType = PetData.Type.HORSE_COLOUR)
    @Override
    public Horse.Color getColor() {
        return getEntity().getColor();
    }

    @AttributeHandler(dataType = PetData.Type.HORSE_STYLE)
    @Override
    public Horse.Style getStyle() {
        return getEntity().getStyle();
    }

    @AttributeHandler(dataType = PetData.Type.HORSE_ARMOUR)
    @Override
    public Armour getArmour() {
        return getEntity().getArmour();
    }

    @AttributeHandler(data = PetData.SADDLE)
    @Override
    public boolean isSaddled() {
        return getEntity().isSaddled();
    }

    @AttributeHandler(data = PetData.CHESTED)
    @Override
    public boolean isChested() {
        return getEntity().isChested();
    }

    @Override
    public SizeCategory getSizeCategory() {
        return isBaby() ? SizeCategory.TINY : SizeCategory.LARGE;
    }

    @Override
    public String getIdleSound() {
        Horse.Variant variant = getVariant();
        return "mob.horse" + (variant == Horse.Variant.UNDEAD_HORSE ? ".zombie" : (variant == Horse.Variant.SKELETON_HORSE ? ".skeleton" : (variant == Horse.Variant.DONKEY || variant == Horse.Variant.MULE ? ".donkey" : ""))) + "death";
    }

    @Override
    public String getDeathSound() {
        Horse.Variant variant = getVariant();
        return "mob.horse" + (variant == Horse.Variant.UNDEAD_HORSE ? ".zombie" : (variant == Horse.Variant.SKELETON_HORSE ? ".skeleton" : (variant == Horse.Variant.DONKEY || variant == Horse.Variant.MULE ? ".donkey" : ""))) + "idle";
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