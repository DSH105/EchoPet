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

package io.github.dsh105.echopet.entity.type.villager;

import io.github.dsh105.echopet.entity.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager.Profession;

@EntityPetType(petType = PetType.VILLAGER)
public class VillagerPet extends Pet implements IAgeablePet {

    boolean baby = false;
    Profession profession = Profession.FARMER;

    public VillagerPet(Player owner) {
        super(owner);
    }

    public VillagerPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }

    @Override
    public boolean isBaby() {
        return this.baby;
    }

    @Override
    public void setBaby(boolean flag) {
        ((EntityVillagerPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    public Profession getProfession() {
        return profession;
    }

    public int getProfessionId() {
        return profession.getId();
    }

    public void setProfession(Profession prof) {
        ((EntityVillagerPet) getEntityPet()).setProfession(prof.getId());
        this.profession = prof;
    }

}
