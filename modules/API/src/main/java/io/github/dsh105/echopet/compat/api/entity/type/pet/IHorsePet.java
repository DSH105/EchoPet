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

package io.github.dsh105.echopet.compat.api.entity.type.pet;

import io.github.dsh105.echopet.compat.api.entity.*;

public interface IHorsePet extends IAgeablePet {

    public void setHorseType(HorseType type);

    public void setVariant(HorseVariant variant, HorseMarking marking);

    public void setArmour(HorseArmour armour);

    public boolean isBaby();

    public void setBaby(boolean flag);

    public void setSaddled(boolean flag);

    public void setChested(boolean flag);

    public HorseType getHorseType();

    public HorseVariant getVariant();

    public HorseMarking getMarking();

    public HorseArmour getArmour();

    public boolean isSaddled();

    public boolean isChested();
}