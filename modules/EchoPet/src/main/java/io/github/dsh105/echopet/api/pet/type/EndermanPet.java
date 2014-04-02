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
import io.github.dsh105.echopet.compat.api.entity.type.nms.IEntityEndermanPet;
import io.github.dsh105.echopet.compat.api.entity.type.pet.IEndermanPet;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.ENDERMAN)
public class EndermanPet extends Pet implements IEndermanPet {

    boolean scream;

    public EndermanPet(Player owner) {
        super(owner);
    }

    public EndermanPet(String owner, IEntityPet entityPet) {
        super(owner, entityPet);
    }

    @Override
    public void setScreaming(boolean flag) {
        ((IEntityEndermanPet) getEntityPet()).setScreaming(flag);
        this.scream = flag;
    }

    @Override
    public boolean isScreaming() {
        return this.scream;
    }
}
