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

import com.dsh105.echopet.api.entity.PetInfo;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.nms.type.EntityHumanPet;
import com.dsh105.echopet.api.entity.pet.PacketPetImpl;
import com.dsh105.echopet.util.protocol.wrapper.WrappedGameProfile;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@PetInfo(type = PetType.HUMAN, width = 0.6F, height = 1.8F)
public class HumanPetImpl extends PacketPetImpl implements HumanPet {

    public HumanPetImpl(Player owner) {
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

    @Override
    public boolean setPetName(String name, boolean sendFailMessage) {
        name = name.length() > 16 ? name.substring(0, 16) : name;
        boolean success = super.setPetName(name, sendFailMessage);
        this.updateName(name);
        return success;
    }

    @Override
    public boolean setPetName(String name) {
        name = name.length() > 16 ? name.substring(0, 16) : name;
        boolean success = super.setPetName(name);
        this.updateName(name);
        return success;
    }

    private void updateName(String name) {
        if (this.getEntityPet().hasInititiated()) {
            this.getEntityPet().updatePosition();
        }
        EntityHumanPet human = (EntityHumanPet) this.getEntityPet();
        if (human.getGameProfile() != null) {
            human.setGameProfile(WrappedGameProfile.getNewProfile(human.getGameProfile(), name));
        }
    }

    @Override
    public boolean teleport(Location to) {
        boolean success = super.teleport(to);
        if (this.getEntityPet().hasInititiated()) {
            this.getEntityPet().updatePosition();
        }
        return success;
    }
}
