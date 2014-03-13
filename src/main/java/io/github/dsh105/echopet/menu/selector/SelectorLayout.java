package io.github.dsh105.echopet.menu.selector;

import com.dsh105.dshutils.config.YAMLConfig;
import com.dsh105.dshutils.util.EnumUtil;
import io.github.dsh105.echopet.config.ConfigOptions;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectorLayout {

    private static ArrayList<SelectorIcon> selectorLayout = new ArrayList<SelectorIcon>();

    public static ItemStack getSelectorItem() {
        YAMLConfig config = ConfigOptions.instance.getConfig();
        String name = config.getString("petSelector.item.name", "&aPets");
        int materialId = config.getInt("petSelector.item.materialId", Material.BONE.getId());
        int materialData = config.getInt("petSelector.item.materialData", 0);
        String l = config.getString("petSelector.item.lore");
        String[] lore = new String[]{l};
        if (l.contains(";")) {
            lore = l.split(";");
        }
        ItemStack i = new ItemStack(materialId, 1, (short) materialData);
        if (i == null) {
            return SelectorItem.SELECTOR.getItem();
        }
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        if (lore != null && lore.length > 0) {
            ArrayList<String> list = new ArrayList<String>();
            for (String s : lore) {
                list.add(ChatColor.translateAlternateColorCodes('&', s));
            }
            meta.setLore(list);
        }
        i.setItemMeta(meta);
        return i;
    }

    public static void loadLayout() {
        YAMLConfig config = ConfigOptions.instance.getConfig();
        String s = "petSelector.menu";
        int size = config.getInt(s + ".slots");
        for (int i = 1; i <= size; i++) {
            String cmd = config.getString(s + ".slot-" + i + ".command");
            String petType = config.getString(s + ".slot-" + i + ".petType");
            PetType pt = null;
            if (petType != null && EnumUtil.isEnumType(PetType.class, petType.toUpperCase())) {
                pt = PetType.valueOf(petType.toUpperCase());
            }
            int id = config.getInt(s + ".slot-" + i + ".materialId");
            int data = config.getInt(s + ".slot-" + i + ".materialData");
            String name = config.getString(s + ".slot-" + i + ".name");
            if (name == null) {
                continue;
            }
            String l = config.getString(s + ".slot-" + i + ".lore");
            String[] lore;
            if (l.contains(";")) {
                lore = l.split(";");
                for (int index = 0; index < lore.length; index++) {
                    lore[index] = ChatColor.translateAlternateColorCodes('&', lore[index]);
                }
            } else {
                lore = new String[]{ChatColor.translateAlternateColorCodes('&', l)};
            }
            if (lore == null || lore.length <= 0 || lore[0] == "") {
                selectorLayout.add(new SelectorIcon(i - 1, cmd, pt, id, data, name));
            } else {
                selectorLayout.add(new SelectorIcon(i - 1, cmd, pt, id, data, name, lore));
            }
        }
    }

    public static HashMap<Integer, SelectorIcon> getLoadedLayout() {
        HashMap<Integer, SelectorIcon> layout = new HashMap<Integer, SelectorIcon>();
        for (SelectorIcon icon : selectorLayout) {
            if (!ConfigOptions.instance.getConfig().getBoolean("petSelector.showDisabledPets", true) && icon.getPetType() != null) {
                if (!ConfigOptions.instance.allowPetType(icon.getPetType())) {
                    continue;
                }
            }
            layout.put(icon.getSlot(), icon);
        }
        return layout;
    }

    public static ArrayList<SelectorIcon> getDefaultLayout() {
        ArrayList<SelectorIcon> layout = new ArrayList<SelectorIcon>();
        int count = 0;
        for (PetItem item : PetItem.values()) {
            layout.add(new SelectorIcon(count, item.getCommand(), item.petType, item.getMat().getId(), item.getData(), item.getName()));
            count++;
        }

        SelectorItem[] selectorItems = new SelectorItem[]{SelectorItem.CLOSE, null, SelectorItem.TOGGLE, SelectorItem.CALL, null, SelectorItem.HAT, SelectorItem.RIDE, SelectorItem.NAME, SelectorItem.MENU};
        int i = 0;
        for (int j = 1; j < 10; j++) {
            SelectorItem s = selectorItems[i++];
            if (s != null) {
                layout.add(new SelectorIcon((45 - j), s.getCommand(), null, s.getMat().getId(), s.getData(), s.getName()));
            }
        }
        return layout;
    }
}