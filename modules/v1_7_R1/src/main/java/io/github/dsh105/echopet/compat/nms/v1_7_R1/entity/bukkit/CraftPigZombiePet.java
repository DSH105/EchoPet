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

package io.github.dsh105.echopet.compat.nms.v1_7_R1.entity.bukkit;

import io.github.dsh105.echopet.compat.api.entity.EntityPetType;
import io.github.dsh105.echopet.compat.api.entity.IPet;
import io.github.dsh105.echopet.compat.api.entity.PetType;
import io.github.dsh105.echopet.compat.api.entity.type.pet.IPigZombiePet;
import io.github.dsh105.echopet.compat.nms.v1_7_R1.entity.CraftPet;
import io.github.dsh105.echopet.compat.nms.v1_7_R1.entity.EntityPet;
import org.bukkit.entity.PigZombie;

@EntityPetType(petType = PetType.PIGZOMBIE)
public class CraftPigZombiePet extends CraftPet implements PigZombie {

    public CraftPigZombiePet(EntityPet entity) {
        super(entity);
    }

    @Override
    public int getAnger() {
        return 0; // Doesn't apply to Pets
    }

    @Override
    public void setAnger(int i) {
        // Doesn't apply to Pets
    }

    @Override
    public void setAngry(boolean b) {
        // Doesn't apply to Pets
    }

    @Override
    public boolean isAngry() {
        return false; // Doesn't apply to Pets
    }

    @Override
    public boolean isBaby() {
        IPet p = this.getPet();
        if (p instanceof IPigZombiePet) {
            return ((IPigZombiePet) p).isBaby();
        }
        return false;
    }

    @Override
    public void setBaby(boolean b) {
        /*Pet p = this.getPet();
        if (p instanceof PigZombiePet) {
            ((PigZombiePet) p).setBaby(b);
        }*/
    }

    @Override
    public boolean isVillager() {
        IPet p = this.getPet();
        if (p instanceof IPigZombiePet) {
            return ((IPigZombiePet) p).isBaby();
        }
        return false;
    }

    @Override
    public void setVillager(boolean b) {
        /*Pet p = this.getPet();
        if (p instanceof PigZombiePet) {
            ((PigZombiePet) p).setVillager(b);
        }*/
    }
}