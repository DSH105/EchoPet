package io.github.dsh105.echopet.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class FancyItemUtil {

    public static ItemStack getItem(String[] content) {
        ItemStack i = new ItemStack(Material.SNOW, 1, (short) 0);
        ItemMeta meta = i.getItemMeta();
        if (meta != null) {
            if (content.length > 0) {
                meta.setDisplayName(content[0]);
            }
            if (content.length > 1) {
                ArrayList<String> list = new ArrayList<String>();
                for (int index = 1; index < content.length; index++) {
                    list.add(ChatColor.WHITE + content[index]);
                }
                meta.setLore(list);
            }
            i.setItemMeta(meta);
        }
        return i;
    }
}