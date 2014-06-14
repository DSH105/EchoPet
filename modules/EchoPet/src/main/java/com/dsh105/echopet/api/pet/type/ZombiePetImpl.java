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

package com.dsh105.echopet.api.pet.type;

import com.dsh105.echopet.api.pet.AgeablePetImpl;
import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.nms.type.EntityHumanPet;
import com.dsh105.echopet.compat.api.entity.nms.type.EntityZombiePet;
import com.dsh105.echopet.compat.api.entity.pet.type.ZombiePet;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@PetInfo(type = PetType.ZOMBIE, width = 0.6F, height = 1.8F)
public class ZombiePetImpl extends AgeablePetImpl implements ZombiePet {

    boolean villager = false;

    public ZombiePetImpl(Player owner) {
        super(owner);
    }

    @Override
    public void setEquipment(Material material) {
        ((EntityHumanPet) this.getEntityPet()).setEquipmentId(material.getId());
    }

    @Override
    public Material getEquipment() {
        return Material.getMaterial(((EntityHumanPet) this.getEntityPet()).getEquipmentId());
    }

    @AttributeHandler(data = PetData.VILLAGER)
    @Override
    public void setVillager(boolean flag) {
        ((EntityZombiePet) getEntityPet()).setVillager(flag);
        this.villager = flag;
    }

    @AttributeHandler(data = PetData.VILLAGER, getter = true)
    @Override
    public boolean isVillager() {
        return this.villager;
    }

}
