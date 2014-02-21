package io.github.dsh105.echopet.entity.type.human;

import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.PacketPet;
import io.github.dsh105.echopet.entity.PetType;
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
