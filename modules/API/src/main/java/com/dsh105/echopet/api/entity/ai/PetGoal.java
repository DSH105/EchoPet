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

package com.dsh105.echopet.api.entity.ai;

import com.dsh105.echopet.api.entity.entitypet.EntityPet;
import com.dsh105.echopet.api.entity.pet.Pet;
import org.bukkit.entity.LivingEntity;

public abstract class PetGoal {

    private Pet pet;

    protected void setPet(Pet pet) {
        if (this.pet != null) {
            throw new UnsupportedOperationException("Pet is already set");
        }
        this.pet = pet;
    }

    public Pet getPet() {
        return pet;
    }

    public EntityPet getEntity() {
        return getPet().getEntity();
    }

    public LivingEntity getBukkitEntity() {
        return getPet().getBukkitEntity();
    }

    public abstract PetGoalType getType();

    public abstract String getDefaultKey();

    public abstract boolean shouldStart(); //a

    public boolean shouldContinue() { //b
        return shouldStart();
    }

    public void start() { //c
    }

    public void finish() { //d
    }

    public boolean isContinuous() {
        return true;
    }

    public abstract void tick(); //e
}
