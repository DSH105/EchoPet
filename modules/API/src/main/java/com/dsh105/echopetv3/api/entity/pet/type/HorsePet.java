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

import com.dsh105.echopetv3.api.entity.entitypet.type.EntityHorsePet;
import com.dsh105.echopetv3.api.entity.pet.AgeablePet;
import org.bukkit.entity.Horse;

public interface HorsePet extends AgeablePet<Horse, EntityHorsePet> {

    void setVariant(Horse.Variant variant);

    void setColor(Horse.Color color);

    void setStyle(Horse.Style style);

    void setArmour(Armour armour);

    void setSaddled(boolean flag);

    void setChested(boolean flag);

    Horse.Variant getVariant();

    Horse.Color getColor();

    Horse.Style getStyle();

    Armour getArmour();

    boolean isSaddled();

    boolean isChested();

    enum Armour {

        NONE,
        IRON,
        GOLD,
        DIAMOND
    }
}