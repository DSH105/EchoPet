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

import com.dsh105.echopet.api.pet.PacketPet;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.IEntityPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityHumanPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IHumanPet;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

@EntityPetType(petType = PetType.HUMAN)
public class HumanPet extends PacketPet implements IHumanPet {

    public HumanPet(Player owner) {
        super(owner);
    }

    @Override
    public void setEquipment(Material material) {
        ((IEntityHumanPet) this.getEntityPet()).setEquipmentId(material.getId());
    }

    @Override
    public Material getEquipment() {
        return Material.getMaterial(((IEntityHumanPet) this.getEntityPet()).getEquipmentId());
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
        IEntityHumanPet human = (IEntityHumanPet) this.getEntityPet();
        if (human.getGameProfile() != null) {
            human.setGameProfile(new GameProfile(((GameProfile) human.getGameProfile()).getId(), name));
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
