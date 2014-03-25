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

package io.github.dsh105.echopet.entity.pathfinder.goals;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.pathfinder.PetGoal;
import io.github.dsh105.echopet.entity.pathfinder.PetGoalType;

public class PetGoalFloat extends PetGoal {

    private EntityPet pet;

    public PetGoalFloat(EntityPet pet) {
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
        if (this.pet.aH().nextFloat() < 0.8F) {
            this.pet.getControllerJump().a();
        }
    }
}
