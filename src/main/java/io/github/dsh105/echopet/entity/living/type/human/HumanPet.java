package io.github.dsh105.echopet.entity.living.type.human;

import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.entity.living.LivingPet;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;


public class HumanPet extends LivingPet {

    public HumanPet(Player owner, PetType petType) {
        super(owner, petType);
    }

    public void setEquipment(Material material) {
        ((EntityHumanPet) this.getEntityPet()).equipmentId = material.getId();
    }

    public Material getEquipment() {
        return Material.getMaterial(((EntityHumanPet) this.getEntityPet()).equipmentId);
    }

    @Override
    public void setName(String s) {
        s = s.length() > 16 ? s.substring(0, 16) : s;
        super.setName(s);
        EntityHumanPet human = (EntityHumanPet) this.getEntityPet();
        if (human.profile != null) {
            human.profile = new GameProfile(human.profile.getId(), s);
        }
        if (human.init) {
            ((EntityHumanPet) this.getEntityPet()).updatePacket();
        }
    }

    @Override
    public void teleport(Location to) {
        super.teleport(to);
        if (((EntityHumanPet) this.getEntityPet()).hasInititiated()) {
            ((EntityHumanPet) this.getEntityPet()).updatePacket();
        }
    }
}
