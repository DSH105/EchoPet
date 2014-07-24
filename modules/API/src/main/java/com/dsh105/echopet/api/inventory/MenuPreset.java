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

package com.dsh105.echopet.api.inventory;

import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.api.entity.AttributeAccessor;
import com.dsh105.echopet.api.entity.PetData;
import com.dsh105.echopet.api.entity.PetType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public enum MenuPreset {

    TOGGLE(Material.BONE, ChatColor.YELLOW + "Toggle Pet", "toggle", MenuType.SELECTOR),
    CALL(Material.ENDER_PEARL, ChatColor.YELLOW + "Call Pet", "call", MenuType.SELECTOR),
    RIDE(Material.CARROT_STICK, ChatColor.YELLOW + "Ride Pet", "ride", MenuType.SELECTOR),
    HAT(Material.IRON_HELMET, ChatColor.YELLOW + "Hat Pet", "hat", MenuType.SELECTOR),
    NAME(Material.NAME_TAG, ChatColor.YELLOW + "Name Your Pet", "name", MenuType.SELECTOR),
    MENU(Material.WORKBENCH, ChatColor.YELLOW + "Open PetMenu", "menu", MenuType.SELECTOR),
    CLOSE_SELECTOR(Material.BOOK, ChatColor.YELLOW + "Close", "select", MenuType.SELECTOR),

    BACK(Material.BOOK, "Back", "Return to the main menu.", MenuType.DATA),
    CLOSE_DATA(Material.BOOK, "Close", "Close the Pet Menu", MenuType.DATA),

    ARMOUR(Material.IRON_CHESTPLATE, MenuType.DATA, PetData.Type.HORSE_ARMOUR),
    CAT_TYPE(Material.RAW_FISH, MenuType.DATA, PetData.Type.CAT_TYPE),
    COLOUR(Material.WOOL, MenuType.DATA, PetData.Type.COLOUR),
    MARKING(Material.INK_SACK, MenuType.DATA, PetData.Type.HORSE_STYLE),
    PROFESSION(Material.IRON_AXE, MenuType.DATA, PetData.Type.VILLAGER_PROFESSION),
    SIZE(Material.SLIME_BALL, MenuType.DATA, PetData.Type.SLIME_SIZE),
    TYPE(Material.HAY_BLOCK, MenuType.DATA, PetData.Type.VARIANT),
    VARIANT(Material.LEASH, MenuType.DATA, PetData.Type.HORSE_COLOUR),

    ANGRY(Material.BONE, MenuType.DATA_SECOND_LEVEL, PetData.ANGRY),
    BABY(Material.WHEAT, MenuType.DATA_SECOND_LEVEL, PetData.BABY),
    CHESTED(Material.CHEST, MenuType.DATA_SECOND_LEVEL, PetData.CHESTED),
    FIRE(Material.FIRE, MenuType.DATA_SECOND_LEVEL, PetData.FIRE),
    POWER(Material.BEACON, MenuType.DATA_SECOND_LEVEL, PetData.POWER),
    SADDLE(Material.SADDLE, MenuType.DATA_SECOND_LEVEL, PetData.SADDLE),
    SCREAMING(Material.ENDER_PEARL, MenuType.DATA_SECOND_LEVEL, PetData.SCREAMING),
    SHEARED(Material.SHEARS, MenuType.DATA_SECOND_LEVEL, PetData.SHEARED),
    SHIELD(Material.GLASS, MenuType.DATA_SECOND_LEVEL, PetData.SHIELD),
    TAMED(Material.BONE, MenuType.DATA_SECOND_LEVEL, PetData.TAMED),
    VILLAGER(Material.EMERALD, MenuType.DATA_SECOND_LEVEL, PetData.VILLAGER),
    WITHER(Material.SKULL_ITEM, 1, MenuType.DATA_SECOND_LEVEL, PetData.WITHER),
    SMALL(Material.SLIME_BALL, 1, 0, MenuType.DATA_SECOND_LEVEL, PetData.SMALL),
    MEDIUM(Material.SLIME_BALL, 2, 0, MenuType.DATA_SECOND_LEVEL, PetData.MEDIUM),
    LARGE(Material.SLIME_BALL, 3, 0, MenuType.DATA_SECOND_LEVEL, PetData.LARGE),;

    public static ItemStack SELECTOR_PRESET;

    static {
        SELECTOR_PRESET = new ItemStack(Material.BONE);
        ItemMeta meta = SELECTOR_PRESET.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Pets");
        SELECTOR_PRESET.setItemMeta(meta);
    }

    private Material material;
    private String name;
    private String command;
    private int amount;
    private short materialData;
    private MenuType menuType;
    private PetData.Type dataType;
    private PetData petData;

    MenuPreset(Material material, MenuType menuType, PetData petData) {
        init(material, StringUtil.capitalise(toString().replace("_", " ")), null, 0, menuType, PetData.Type.BOOLEAN, petData);
    }

    MenuPreset(Material material, int materialData, MenuType menuType, PetData petData) {
        init(material, StringUtil.capitalise(toString().replace("_", " ")), null, materialData, menuType, PetData.Type.BOOLEAN, petData);
    }

    MenuPreset(Material material, int amount, int materialData, MenuType menuType, PetData petData) {
        init(material, StringUtil.capitalise(toString().replace("_", " ")), null, amount, materialData, menuType, PetData.Type.BOOLEAN, petData);
    }

    MenuPreset(Material material, MenuType menuType, PetData.Type dataType) {
        init(material, StringUtil.capitalise(toString().replace("_", " ")), null, 0, menuType, dataType, null);
    }

    MenuPreset(Material material, String name, String command, MenuType menuType) {
        init(material, name, command, 0, menuType, null, null);
    }

    private void init(Material material, String name, String command, int materialData, MenuType menuType, PetData.Type dataType, PetData petData) {
        init(material, name, command, 1, materialData, menuType, dataType, petData);
    }

    private void init(Material material, String name, String command, int amount, int materialData, MenuType menuType, PetData.Type dataType, PetData petData) {
        this.material = material;
        this.name = name;
        this.command = "pet " + command;
        this.amount = amount;
        this.materialData = (short) materialData;
        this.menuType = menuType;
        this.dataType = dataType;
        this.petData = petData;
    }

    public String getCommand() {
        return command;
    }

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    public short getMaterialData() {
        return materialData;
    }

    public String getName() {
        return name;
    }

    public PetData.Type getDataType() {
        return dataType;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public PetData getPetData() {
        return petData;
    }

    public static ArrayList<MenuPreset> getPresetsOfType(PetData.Type toCompare) {
        ArrayList<MenuPreset> presets = new ArrayList<>();
        for (MenuPreset preset : MenuPreset.values()) {
            if ((preset.getDataType() != null && preset.getDataType().equals(toCompare)) || preset.getPetData().isType(toCompare)) {
                presets.add(preset);
            }
        }
        return presets;
    }

    public static ArrayList<MenuPreset> getPresetsOfType(MenuType toCompare) {
        ArrayList<MenuPreset> presets = new ArrayList<>();
        for (MenuPreset preset : MenuPreset.values()) {
            if (preset.getMenuType().equals(toCompare)) {
                presets.add(preset);
            }
        }
        return presets;
    }

    public static ArrayList<MenuPreset> getPresets(PetType petType) {
        ArrayList<MenuPreset> presets = new ArrayList<>(Arrays.asList(RIDE, HAT));

        ArrayList<PetData> registeredData = AttributeAccessor.getRegisteredData(petType);
        ArrayList<MenuPreset> dataPresets = getPresetsOfType(MenuType.DATA);
        for (MenuPreset preset : dataPresets) {
            if (presets.contains(preset)) {
                continue;
            }
            for (PetData data : registeredData) {
                if (data == preset.getPetData() || data.isType(preset.getDataType())) {
                    presets.add(preset);
                }
            }
        }
        return presets;
    }

    public enum MenuType {
        SELECTOR, DATA, DATA_SECOND_LEVEL
    }
}