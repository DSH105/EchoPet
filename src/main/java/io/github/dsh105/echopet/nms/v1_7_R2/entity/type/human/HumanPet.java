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

package io.github.dsh105.echopet.nms.v1_7_R2.entity.type.human;

import io.github.dsh105.echopet.nms.v1_7_R2.entity.EntityPet;
import io.github.dsh105.echopet.nms.v1_7_R2.entity.EntityPetType;
import io.github.dsh105.echopet.nms.v1_7_R2.entity.PacketPet;
import io.github.dsh105.echopet.nms.v1_7_R2.entity.PetType;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.HUMAN)
public class HumanPet extends PacketPet {

    public HumanPet(Player owner) {
        super(owner);
    }

    public HumanPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }

    public void setEquipment(Material material) {
        ((EntityHumanPet) this.getEntityPet()).equipmentId = material.getId();
    }

    public Material getEquipment() {
        return Material.getMaterial(((EntityHumanPet) this.getEntityPet()).equipmentId);
    }

    @Override
    public boolean setPetName(String name, boolean sendFailMessage) {
        name = name.length() > 16 ? name.substring(0, 16) : name;
        boolean success = super.setPetName(name, sendFailMessage);
        if (this.getEntityPet().hasInititiated()) {
            this.getEntityPet().updatePacket();
        }
        EntityHumanPet human = (EntityHumanPet) this.getEntityPet();
        if (human.profile != null) {
            human.profile = new GameProfile(human.profile.getId(), name);
        }
        return success;
    }

    @Override
    public boolean setPetName(String name) {
        name = name.length() > 16 ? name.substring(0, 16) : name;
        boolean success = super.setPetName(name);
        if (this.getEntityPet().hasInititiated()) {
            this.getEntityPet().updatePacket();
        }
        EntityHumanPet human = (EntityHumanPet) this.getEntityPet();
        if (human.profile != null) {
            human.profile = new GameProfile(human.profile.getId(), name);
        }
        return success;
    }

    @Override
    public void teleport(Location to) {
        super.teleport(to);
        if (this.getEntityPet().hasInititiated()) {
            this.getEntityPet().updatePacket();
        }
    }
}
