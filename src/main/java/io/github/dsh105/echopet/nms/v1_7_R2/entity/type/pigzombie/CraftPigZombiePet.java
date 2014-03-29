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

package io.github.dsh105.echopet.nms.v1_7_R2.entity.type.pigzombie;

import io.github.dsh105.echopet.nms.v1_7_R2.entity.*;
import org.bukkit.craftbukkit.v1_7_R2.CraftServer;
import org.bukkit.entity.PigZombie;

@EntityPetType(petType = PetType.PIGZOMBIE)
public class CraftPigZombiePet extends CraftPet implements PigZombie {

    public CraftPigZombiePet(CraftServer server, EntityPet entity) {
        super(server, entity);
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
        Pet p = this.getPet();
        if (p instanceof PigZombiePet) {
            return ((PigZombiePet) p).isBaby();
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
        Pet p = this.getPet();
        if (p instanceof PigZombiePet) {
            return ((PigZombiePet) p).isBaby();
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