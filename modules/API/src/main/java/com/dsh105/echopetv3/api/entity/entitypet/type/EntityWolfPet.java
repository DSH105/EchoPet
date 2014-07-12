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

package com.dsh105.echopetv3.api.entity.entitypet.type;

import com.dsh105.echopetv3.api.entity.entitypet.EntityAgeablePet;
import com.dsh105.echopetv3.api.entity.pet.type.WolfPet;
import org.bukkit.DyeColor;

public interface EntityWolfPet extends EntityAgeablePet<WolfPet> {

    public static int DATAWATCHER_ENTITY_STATUS = 16;
    public static int DATAWATCHER_OWNER_NAME = 17;
    public static int DATAWATCHER_HEALTH = 18;
    public static int DATAWATCHER_BEGGING = 19;
    public static int DATAWATCHER_COLLAR_COLOUR = 20;

    void setCollarColor(DyeColor color);

    DyeColor getCollarColor();

    void setTamed(boolean flag);

    boolean isTamed();

    void setAngry(boolean flag);

    boolean isAngry();

    float getTailHealth();

    void shakeParticle(float shakeCount);
}