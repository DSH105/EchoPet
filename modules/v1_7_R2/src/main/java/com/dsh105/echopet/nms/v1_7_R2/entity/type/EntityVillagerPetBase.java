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

package com.dsh105.echopet.nms.v1_7_R2.entity.type;

import com.dsh105.echopet.api.entity.entitypet.type.EntityVillagerPet;
import com.dsh105.echopet.api.entity.pet.type.VillagerPet;
import com.dsh105.echopet.nms.v1_7_R2.entity.EntityAgeablePetBase;
import net.minecraft.server.v1_7_R2.World;
import org.bukkit.entity.Villager;

public class EntityVillagerPetBase extends EntityAgeablePetBase<VillagerPet> implements EntityVillagerPet {

    public EntityVillagerPetBase(World world, VillagerPet pet) {
        super(world, pet);
    }

    @Override
    public Villager.Profession getVillagerProfession() {
        return Villager.Profession.getProfession(this.datawatcher.getInt(DATAWATCHER_PROFESSION));
    }

    @Override
    public void setVillagerProfession(Villager.Profession profession) {
        this.datawatcher.watch(DATAWATCHER_PROFESSION, profession.getId());
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(DATAWATCHER_PROFESSION, new Integer(0));
    }
}