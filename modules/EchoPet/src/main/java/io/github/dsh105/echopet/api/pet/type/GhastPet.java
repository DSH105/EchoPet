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

package io.github.dsh105.echopet.api.pet.type;

import io.github.dsh105.echopet.api.pet.Pet;
import io.github.dsh105.echopet.compat.api.entity.EntityPetType;
import io.github.dsh105.echopet.compat.api.entity.IEntityPet;
import io.github.dsh105.echopet.compat.api.entity.PetType;
import io.github.dsh105.echopet.compat.api.entity.type.pet.IGhastPet;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.GHAST)
public class GhastPet extends Pet implements IGhastPet {

    public GhastPet(Player owner) {
        super(owner);
    }

    public GhastPet(String owner, IEntityPet entityPet) {
        super(owner, entityPet);
    }
}
