package io.github.dsh105.echopet.entity.inanimate.type.human;

import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.entity.inanimate.InanimatePet;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.Material;
import org.bukkit.entity.Player;


public class HumanPet extends InanimatePet {

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
    public void setName(String name) {
        name = name.length() > 16 ? name.substring(0, 16) : name;
        super.setName(name);
        EntityHumanPet human = (EntityHumanPet) this.getEntityPet();
        if (human.profile != null) {
            human.profile = new GameProfile(human.profile.getId(), name);
        }
    }
}
