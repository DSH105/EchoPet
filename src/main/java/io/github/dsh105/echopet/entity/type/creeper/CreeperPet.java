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

package io.github.dsh105.echopet.entity.type.creeper;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.CREEPER)
public class CreeperPet extends Pet {

    boolean powered;
    boolean ignited;

    public CreeperPet(Player owner) {
        super(owner);
    }

    public CreeperPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }

    public void setPowered(boolean flag) {
        ((EntityCreeperPet) getEntityPet()).setPowered(flag);
        this.powered = flag;
    }

    public boolean isPowered() {
        return this.powered;
    }

    public void setIgnited(boolean flag) {
        ((EntityCreeperPet) getEntityPet()).setIgnited(flag);
        this.ignited = flag;
    }

    public boolean isIgnited() {
        return this.ignited;
    }
}
