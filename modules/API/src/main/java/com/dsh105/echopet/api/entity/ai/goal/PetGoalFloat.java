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

package com.dsh105.echopet.api.entity.ai.goal;

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.echopet.api.entity.ai.PetGoal;
import com.dsh105.echopet.api.entity.ai.PetGoalType;

public class PetGoalFloat extends PetGoal {

    @Override
    public PetGoalType getType() {
        return PetGoalType.FOUR;
    }

    @Override
    public String getDefaultKey() {
        return "float";
    }

    @Override
    public boolean shouldStart() {
        return getEntity().inWater() || getEntity().inLava();
    }

    @Override
    public void tick() {
        if (GeneralUtil.random().nextFloat() < 0.8F) {
            getEntity().jump();
        }
    }
}