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

import com.dsh105.echopetv3.api.entity.AttributeHandler;
import com.dsh105.echopetv3.api.entity.PetData;
import com.dsh105.echopetv3.api.entity.PetInfo;
import com.dsh105.echopetv3.api.entity.PetType;
import com.dsh105.echopetv3.api.entity.entitypet.type.EntitySheepPet;
import com.dsh105.echopetv3.api.entity.pet.AgeablePetBase;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

@PetInfo(type = PetType.SHEEP, width = 0.9F, height = 1.3F)
public class SheepPetBase extends AgeablePetBase<Sheep, EntitySheepPet> implements SheepPet {

    public SheepPetBase(Player owner) {
        super(owner);
    }

    @AttributeHandler(data = PetData.SHEARED)
    @Override
    public void setSheared(boolean flag) {
        getEntity().setSheared(flag);
    }

    @AttributeHandler(data = PetData.SHEARED, getter = true)
    @Override
    public boolean isSheared() {
        return getEntity().isSheared();
    }

    @AttributeHandler(dataType = PetData.Type.COLOUR)
    @Override
    public DyeColor getColor() {
        return getEntity().getColor();
    }

    @AttributeHandler(dataType = PetData.Type.COLOUR, getter = true)
    @Override
    public void setColor(DyeColor dyeColor) {
        getEntity().setColor(dyeColor);
    }

    @Override
    public String getIdleSound() {
        return "mob.sheep.say";
    }

    @Override
    public String getDeathSound() {
        return "mob.sheep.say";
    }

    @Override
    public void makeStepSound() {
        getEntity().makeSound("mob.sheep.step", 0.15F, 1.0F);
    }
}