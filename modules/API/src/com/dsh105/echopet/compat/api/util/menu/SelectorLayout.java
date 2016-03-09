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

package com.dsh105.echopet.compat.api.util.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.commodus.config.YAMLConfig;
import com.dsh105.echopet.compat.api.config.ConfigOptions;
import com.dsh105.echopet.compat.api.config.PetItem;
import com.dsh105.echopet.compat.api.entity.PetType;

public class SelectorLayout {

    private static ArrayList<SelectorIcon> selectorLayout = new ArrayList<SelectorIcon>();
    private static SelectorMenu selectorMenu;

    public static ItemStack getSelectorItem() {
        YAMLConfig config = ConfigOptions.instance.getConfig();
        String name = config.getString("petSelector.item.name", "&aPets");
        int materialId = config.getInt("petSelector.item.materialId", Material.BONE.getId());
        int materialData = config.getInt("petSelector.item.materialData", 0);
        List<String> lore = config.config().getStringList("petSelector.item.lore");
        if (lore == null) {
            lore = new ArrayList<String>();
        }
        ItemStack i = new ItemStack(materialId, 1, (short) materialData);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        ArrayList<String> loreList = new ArrayList<String>();
        if (lore.size() > 0) {
            for (String s : lore) {
                loreList.add(ChatColor.translateAlternateColorCodes('&', s));
            }
        }
        if (!loreList.isEmpty()) {
            meta.setLore(loreList);
        }
        i.setItemMeta(meta);
        return i;
    }

    public static void loadLayout() {
        if (selectorMenu != null) {
            HandlerList.unregisterAll(selectorMenu);
        }
        selectorLayout.clear();
        YAMLConfig config = ConfigOptions.instance.getConfig();
        String s = "petSelector.menu";
        int size = config.getInt(s + ".slots");
        for (int i = 1; i <= size; i++) {
            String cmd = config.getString(s + ".slot-" + i + ".command");
            String petType = config.getString(s + ".slot-" + i + ".petType");
            PetType pt = null;
            if (petType != null && GeneralUtil.isEnumType(PetType.class, petType.toUpperCase())) {
                pt = PetType.valueOf(petType.toUpperCase());
            }
			Material material = Material.getMaterial(config.getString(s + ".slot-" + i + ".material"));
			int data = config.getInt(s + ".slot-" + i + ".materialData", -1);// Support old configs
			String entityTag = "Pig";
			if(data > 0){
				EntityType et = EntityType.fromId(data);
				if(et != null){
					entityTag = et.getName();
				}
			}else{
				entityTag = config.getString(s + ".slot-" + i + ".entityName", "Pig");
			}
            String name = config.getString(s + ".slot-" + i + ".name");
            if (name == null) {
                continue;
            }
            List<String> lore = config.config().getStringList(s + ".slot-" + i + ".lore");
            if (lore == null) {
                lore = new ArrayList<String>();
            }
            ArrayList<String> loreList = new ArrayList<String>();
            if (lore.size() > 0) {
                for (String part : lore) {
                    loreList.add(ChatColor.translateAlternateColorCodes('&', part));
                }
            }
			if(material.equals(Material.MONSTER_EGG)) selectorLayout.add(new SelectorIcon(i - 1, cmd, pt, material, entityTag, name, loreList.toArray(new String[0])));
			else selectorLayout.add(new SelectorIcon(i - 1, cmd, pt, material, data, name, loreList.toArray(new String[0])));
        }

        selectorMenu = new SelectorMenu();
    }

    public static SelectorMenu getSelectorMenu() {
        return selectorMenu;
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
			if(item.getPetType() != null && !item.getPetType().equals(PetType.HUMAN) && !(item.getMaterialData() > 0)) layout.add(new SelectorIcon(count, item.getCommand(), item.petType, item.getMat(), item.getPetType().getEntityType().getName(), item.getName()));
			else layout.add(new SelectorIcon(count, item.getCommand(), item.petType, item.getMat(), item.getMaterialData(), item.getName()));
			count++;
        }

        SelectorItem[] selectorItems = new SelectorItem[]{SelectorItem.CLOSE, null, SelectorItem.TOGGLE, SelectorItem.CALL, null, SelectorItem.HAT, SelectorItem.RIDE, SelectorItem.NAME, SelectorItem.MENU};
        int i = 0;
        for (int j = 1; j < 10; j++) {
            SelectorItem s = selectorItems[i++];
            if (s != null) {
				layout.add(new SelectorIcon((45 - j), s.getCommand(), null, s.getMat(), s.getData(), s.getName()));
            }
        }
        return layout;
    }
}