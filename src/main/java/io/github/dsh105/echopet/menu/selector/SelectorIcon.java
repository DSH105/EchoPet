package io.github.dsh105.echopet.menu.selector;

import com.dsh105.dshutils.inventory.MenuIcon;
import io.github.dsh105.echopet.entity.PetType;
import io.github.dsh105.echopet.util.PetUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class SelectorIcon extends MenuIcon {

    private String command;
    private PetType petType;

    public SelectorIcon(int slot, String command, PetType petType, int materialId, int materialData, String name, String... lore) {
        super(slot, materialId, materialData, name, lore);
        this.command = command;
        this.petType = petType;
    }

    public String getCommand() {
        return command;
    }

    public PetType getPetType() {
        return petType;
    }

    @Override
    public ItemStack getIcon(Player viewer) {
        ItemStack i = super.getIcon(viewer);
        ItemMeta meta = i.getItemMeta();
        ChatColor c = this.petType == null ? ChatColor.YELLOW : (viewer.hasPermission("echopet.pet.type." + PetUtil.getPetPerm(this.getPetType()))) ? ChatColor.GREEN : ChatColor.RED;
        meta.setDisplayName(c + this.getName());
        i.setItemMeta(meta);

        if (this.petType == PetType.HUMAN && i.getItemMeta() instanceof SkullMeta) {
            SkullMeta sm = (SkullMeta) i.getItemMeta();
            sm.setOwner(viewer.getName());
            i.setItemMeta(sm);
        }
        return i;
    }

    public void onClick(Player viewer) {
        viewer.performCommand(this.getCommand());
    }
}