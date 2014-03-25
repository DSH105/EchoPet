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

package io.github.dsh105.echopet.entity.type.sheep;

import io.github.dsh105.echopet.entity.*;
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

    public SheepPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }

    @Override
    public void setBaby(boolean flag) {
        ((EntitySheepPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    @Override
    public boolean isBaby() {
        return this.baby;
    }

    public void setSheared(boolean flag) {
        ((EntitySheepPet) getEntityPet()).setSheared(flag);
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
        ((EntitySheepPet) getEntityPet()).setColor(c.getWoolData());
        this.color = c.getWoolData();
    }

    public void setColor(byte b) {
        ((EntitySheepPet) getEntityPet()).setColor(b);
        this.color = b;
    }

}
