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

package io.github.dsh105.echopet.api.entity.pet.type;

import io.github.dsh105.echopet.api.entity.EntityPetType;
import io.github.dsh105.echopet.api.entity.IAgeablePet;
import io.github.dsh105.echopet.api.entity.PetType;
import io.github.dsh105.echopet.api.entity.nms.IEntityPet;
import io.github.dsh105.echopet.api.entity.nms.type.IEntitySheepPet;
import io.github.dsh105.echopet.api.entity.pet.Pet;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.SHEEP)
public class SheepPet extends Pet implements IAgeablePet {

    boolean baby;
    boolean sheared;
    byte color;

    public SheepPet(Player owner) {
        super(owner);
    }

    public SheepPet(String owner, IEntityPet entityPet) {
        super(owner, entityPet);
    }

    @Override
    public void setBaby(boolean flag) {
        ((IEntitySheepPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    @Override
    public boolean isBaby() {
        return this.baby;
    }

    public void setSheared(boolean flag) {
        ((IEntitySheepPet) getEntityPet()).setSheared(flag);
        this.sheared = flag;
    }

    public boolean isSheared() {
        return this.sheared;
    }

    public DyeColor getColor() {
        return org.bukkit.DyeColor.getByWoolData(color);
    }

    public byte getColorByte() {
        return color;
    }

    public void setColor(DyeColor c) {
        ((IEntitySheepPet) getEntityPet()).setColor(c.getWoolData());
        this.color = c.getWoolData();
    }

    public void setColor(byte b) {
        ((IEntitySheepPet) getEntityPet()).setColor(b);
        this.color = b;
    }

}
