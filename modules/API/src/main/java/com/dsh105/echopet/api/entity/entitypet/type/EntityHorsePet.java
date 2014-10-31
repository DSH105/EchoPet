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

package com.dsh105.echopet.api.entity.entitypet.type;

import com.dsh105.echopet.api.entity.entitypet.EntityAgeablePet;
import com.dsh105.echopet.api.entity.pet.type.HorsePet;
import org.bukkit.entity.Horse;

public interface EntityHorsePet extends EntityAgeablePet<HorsePet> {

    public static final int DATAWATCHER_ANIMATION = 16;
    public static final int DATAWATCHER_HORSE_VARIANT = 19;
    public static final int DATAWATCHER_HORSE_SKIN = 20;
    public static final int DATAWATCHER_OWNER_NAME = 21;
    public static final int DATAWATCHER_HORSE_ARMOUR = 22;

    public static final int ANIMATION_SADDLE = 4;
    public static final int ANIMATION_CHEST = 8;
    public static final int ANIMATION_LOWER_HEAD = 32;
    public static final int ANIMATION_REAR = 64;
    public static final int ANIMATION_OPEN_MOUTH = 128;

    Horse.Variant getHorseVariant();

    Horse.Color getColor();

    Horse.Style getStyle();

    HorsePet.Armour getArmour();

    boolean isSaddled();

    boolean isChested();

    void setHorseVariant(Horse.Variant variant);

    void setColor(Horse.Color color);

    void setStyle(Horse.Style style);

    void setArmour(HorsePet.Armour armour);

    void setSaddled(boolean flag);

    void setChested(boolean flag);

    void animation(int animationId, boolean flag);

    public void setTame(boolean flag);
}