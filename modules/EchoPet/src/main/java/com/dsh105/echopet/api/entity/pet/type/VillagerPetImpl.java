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

package com.dsh105.echopet.api.entity.pet.type;

import com.dsh105.echopet.api.entity.AttributeHandler;
import com.dsh105.echopet.api.entity.PetData;
import com.dsh105.echopet.api.entity.PetInfo;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.nms.type.EntityVillagerPet;
import com.dsh105.echopet.api.entity.pet.AgeablePetImpl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager.Profession;

@PetInfo(type = PetType.VILLAGER, width = 0.6F, height = 1.8F)
public class VillagerPetImpl extends AgeablePetImpl implements VillagerPet {

    Profession profession = Profession.FARMER;

    public VillagerPetImpl(Player owner) {
        super(owner);
    }

    @AttributeHandler(dataType = PetData.Type.VILLAGER_PROFESSION)
    @Override
    public Profession getProfession() {
        return profession;
    }

    @AttributeHandler(dataType = PetData.Type.VILLAGER_PROFESSION, getter = true)
    @Override
    public void setProfession(Profession prof) {
        ((EntityVillagerPet) getEntityPet()).setProfession(prof.getId());
        this.profession = prof;
    }

    @Override
    public int getProfessionId() {
        return profession.getId();
    }

}
