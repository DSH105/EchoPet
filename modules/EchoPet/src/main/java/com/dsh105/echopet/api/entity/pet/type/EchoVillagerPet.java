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

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.echopet.api.entity.AttributeHandler;
import com.dsh105.echopet.api.entity.PetData;
import com.dsh105.echopet.api.entity.PetInfo;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.entitypet.type.EntityVillagerPet;
import com.dsh105.echopet.api.entity.pet.EchoAgeablePet;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

@PetInfo(type = PetType.VILLAGER, width = 0.6F, height = 1.8F)
public class EchoVillagerPet extends EchoAgeablePet<Villager, EntityVillagerPet> implements VillagerPet {

    public EchoVillagerPet(Player owner) {
        super(owner);
    }

    @AttributeHandler(dataType = PetData.Type.VILLAGER_PROFESSION)
    @Override
    public Villager.Profession getProfession() {
        return getEntity().getVillagerProfession();
    }

    @AttributeHandler(dataType = PetData.Type.VILLAGER_PROFESSION, getter = true)
    @Override
    public void setProfession(Villager.Profession profession) {
        getEntity().setVillagerProfession(profession);
    }

    @Override
    public String getIdleSound() {
        return GeneralUtil.random().nextBoolean() ? "mob.villager.haggle" : "mob.villager.idle";
    }

    @Override
    public String getDeathSound() {
        return "mob.villager.death";
    }
}