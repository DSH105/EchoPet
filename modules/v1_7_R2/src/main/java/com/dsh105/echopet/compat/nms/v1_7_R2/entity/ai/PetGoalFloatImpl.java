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

package com.dsh105.echopet.compat.nms.v1_7_R2.entity.ai;

import com.dsh105.echopet.compat.api.ai.PetGoalFloat;
import com.dsh105.echopet.compat.api.ai.PetGoalType;
import com.dsh105.echopet.compat.nms.v1_7_R2.entity.EntityPetImpl;

public class PetGoalFloatImpl extends PetGoalFloat {

    private EntityPetImpl pet;

    public PetGoalFloatImpl(EntityPetImpl pet) {
        this.pet = pet;
        pet.getNavigation().e(true);
    }

    @Override
    public PetGoalType getType() {
        return PetGoalType.FOUR;
    }

    @Override
    public String getDefaultKey() {
        return "Float";
    }

    @Override
    public boolean shouldStart() {
        return this.pet.L() || this.pet.O();
    }

    @Override
    public void tick() {
        if (this.pet.random().nextFloat() < 0.8F) {
            this.pet.getControllerJump().a();
        }
    }
}
