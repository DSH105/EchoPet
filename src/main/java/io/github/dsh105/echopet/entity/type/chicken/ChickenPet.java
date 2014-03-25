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

package io.github.dsh105.echopet.entity.type.chicken;


import io.github.dsh105.echopet.entity.*;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.CHICKEN)
public class ChickenPet extends Pet implements IAgeablePet {

    boolean baby;

    public ChickenPet(Player owner) {
        super(owner);
    }

    public ChickenPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }

    public void setBaby(boolean flag) {
        ((EntityChickenPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    public boolean isBaby() {
        return this.baby;
    }
}