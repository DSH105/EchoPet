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

package io.github.dsh105.echopet.entity.type.wither;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.WITHER)
public class WitherPet extends Pet {

    boolean shield = false;

    public WitherPet(Player owner) {
        super(owner);
    }

    public WitherPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }

    public void setShielded(boolean flag) {
        ((EntityWitherPet) getEntityPet()).setShielded(flag);
        this.shield = flag;
    }

    public boolean isShielded() {
        return this.shield;
    }

}
