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
import com.dsh105.commodus.container.ItemStackContainer;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.attribute.AttributeManager;
import com.dsh105.echopet.api.entity.attribute.AttributeType;
import com.dsh105.echopet.api.entity.attribute.Attributes;
import com.dsh105.echopet.api.entity.attribute.EntityAttribute;
import com.dsh105.interact.Interact;
import com.dsh105.interact.api.Icon;
import org.bukkit.ChatColor;

import java.util.*;

public enum MenuPreset {

    TOGGLE("bone", ChatColor.YELLOW + "Toggle Pet", "toggle", MenuType.SELECTOR),
    CALL("ender_pearl", ChatColor.YELLOW + "Call Pet", "call", MenuType.SELECTOR),
    RIDE("carrot_on_a_stick", ChatColor.YELLOW + "Ride Pet", "ride", MenuType.SELECTOR),
    HAT("iron_helmet", ChatColor.YELLOW + "Hat Pet", "hat", MenuType.SELECTOR),
    NAME("name_tag", ChatColor.YELLOW + "Name Your Pet", "name", MenuType.SELECTOR),
    MENU("crafting_table", ChatColor.YELLOW + "Open PetMenu", "menu", MenuType.SELECTOR),
    CLOSE_SELECTOR("book", ChatColor.YELLOW + "Close", "select", MenuType.SELECTOR),

    BACK("book", "Back", "Return to the main menu.", MenuType.DATA),
    CLOSE_DATA("book", "Close", "Close the Pet Menu", MenuType.DATA),

    /*
     * Horse armor
     */
    HORSE_ARMOR("armor_stand", MenuType.DATA, AttributeType.HORSE_ARMOUR),
    HORSE_ARMOR_NONE("armor_stand", MenuType.DATA, Attributes.HorseArmour.NONE),
    HORSE_ARMOR_IRON("iron_horse_armor", MenuType.DATA, Attributes.HorseArmour.NONE),
    HORSE_ARMOR_GOLD("golden_horse_armor", MenuType.DATA, Attributes.HorseArmour.NONE),
    HORSE_ARMOR_DIAMOND("diamond_horse_armor", MenuType.DATA, Attributes.HorseArmour.NONE),

    /*
     * Horse variants
     */
    HORSE_VARIANT("hay_block", MenuType.DATA, AttributeType.HORSE_VARIANT),
    HORSE_VARIANT_NORMAL("hay_block", MenuType.DATA, Attributes.HorseVariant.NORMAL),
    HORSE_VARIANT_DONKEY("chest", MenuType.DATA, Attributes.HorseVariant.DONKEY),
    HORSE_VARIANT_MULE("chest", MenuType.DATA, Attributes.HorseVariant.MULE),
    HORSE_VARIANT_ZOMBIE("rotten_flesh", MenuType.DATA, Attributes.HorseVariant.ZOMBIE),
    HORSE_VARIANT_SKELETON("bone", MenuType.DATA, Attributes.HorseVariant.SKELETON),

    /*
     * Horse styles
     */
    HORSE_STYLE("lead", MenuType.DATA, AttributeType.HORSE_STYLE),
    HORSE_STYLE_NONE("lead", MenuType.DATA, Attributes.HorseStyle.NONE),
    HORSE_STYLE_WHITE("wool", MenuType.DATA, Attributes.HorseStyle.WHITE),
    HORSE_STYLE_WHITEFIELD("wool", MenuType.DATA, Attributes.HorseStyle.WHITEFIELD),
    HORSE_STYLE_WHITE_DOTS("wool", MenuType.DATA, Attributes.HorseStyle.WHITE_DOTS),
    HORSE_STYLE_BLACK_DOTS("wool", 15, MenuType.DATA, Attributes.HorseStyle.BLACK_DOTS),

    /*
     * Horse colors
     */
    HORSE_COLOR("dye", MenuType.DATA, AttributeType.HORSE_COLOUR),
    HORSE_COLOR_WHITE("dye", MenuType.DATA, Attributes.HorseColor.WHITE),
    HORSE_COLOR_CREAMY("dye", MenuType.DATA, Attributes.HorseColor.CREAMY),
    HORSE_COLOR_CHESTNUT("dye", MenuType.DATA, Attributes.HorseColor.CHESTNUT),
    HORSE_COLOR_BROWN("dye", 12, MenuType.DATA, Attributes.HorseColor.BROWN),
    HORSE_COLOR_BLACK("dye", 15, MenuType.DATA, Attributes.HorseColor.BLACK),
    HORSE_COLOR_GRAY("dye", 8, MenuType.DATA, Attributes.HorseColor.GRAY),
    HORSE_COLOR_DARK_BROWN("dye", 12, MenuType.DATA, Attributes.HorseColor.DARK_BROWN),

    /*
     * Ocelot types
     */
    OCELOT_TYPE("fish", MenuType.DATA, AttributeType.OCELOT_TYPE),
    OCELOT_TYPE_WILD("fish", MenuType.DATA, Attributes.OcelotType.WILD),
    OCELOT_TYPE_BLACK("wool", 15, MenuType.DATA, Attributes.OcelotType.BLACK),
    OCELOT_TYPE_RED("wool", 14, MenuType.DATA, Attributes.OcelotType.RED),
    OCELOT_TYPE_SIAMESE("fish", MenuType.DATA, Attributes.OcelotType.SIAMESE),

    /*
     * Colors
     */
    COLOR("wool", MenuType.DATA, AttributeType.COLOR),
    COLOR_WHITE("wool", MenuType.DATA, Attributes.Color.WHITE),
    COLOR_ORANGE("wool", 1, MenuType.DATA, Attributes.Color.ORANGE),
    COLOR_MAGENTA("wool", 2, MenuType.DATA, Attributes.Color.MAGENTA),
    COLOR_LIGHT_BLUE("wool", 3, MenuType.DATA, Attributes.Color.LIGHT_BLUE),
    COLOR_YELLOW("wool", 4, MenuType.DATA, Attributes.Color.YELLOW),
    COLOR_LIME("wool", 5, MenuType.DATA, Attributes.Color.LIME),
    COLOR_PINK("wool", 6, MenuType.DATA, Attributes.Color.PINK),
    COLOR_GRAY("wool", 7, MenuType.DATA, Attributes.Color.GRAY),
    COLOR_SILVER("wool", 8, MenuType.DATA, Attributes.Color.SILVER),
    COLOR_CYAN("wool", 9, MenuType.DATA, Attributes.Color.CYAN),
    COLOR_PURPLE("wool", 10, MenuType.DATA, Attributes.Color.PURPLE),
    COLOR_BLUE("wool", 11, MenuType.DATA, Attributes.Color.BLUE),
    COLOR_BROWN("wool", 12, MenuType.DATA, Attributes.Color.BROWN),
    COLOR_GREEN("wool", 13, MenuType.DATA, Attributes.Color.GREEN),
    COLOR_RED("wool", 14, MenuType.DATA, Attributes.Color.RED),
    COLOR_BLACK("wool", 15, MenuType.DATA, Attributes.Color.BLACK),

    /*
     * Professions
     */
    PROFESSION("iron_axe", MenuType.DATA, AttributeType.PROFESSION),
    PROFESSION_FARMER("wooden_hoe", MenuType.DATA, Attributes.VillagerProfession.FARMER),
    PROFESSION_LIBRARIAN("book", MenuType.DATA, Attributes.VillagerProfession.LIBRARIAN),
    PROFESSION_PRIEST("water_bucket", MenuType.DATA, Attributes.VillagerProfession.PRIEST),
    PROFESSION_BLACKSMITH("iron_pickaxe", MenuType.DATA, Attributes.VillagerProfession.BLACKSMITH),
    PROFESSION_BUTCHER("cooked_beef", MenuType.DATA, Attributes.VillagerProfession.BUTCHER),

    
    /*
     * Careers
     */
    CAREER("gold_axe", MenuType.DATA, AttributeType.CAREER),
    CAREER_FARMER("wooden_hoe", MenuType.DATA, Attributes.VillagerCareer.FARMER),
    CAREER_FISHERMAN("fish", MenuType.DATA, Attributes.VillagerCareer.FISHERMAN),
    CAREER_SHEPHERD("lead", MenuType.DATA, Attributes.VillagerCareer.SHEPHERD),
    CAREER_FLETCHER("arrow", MenuType.DATA, Attributes.VillagerCareer.FLETCHER),
    CAREER_LIBRARIAN("book", MenuType.DATA, Attributes.VillagerCareer.LIBRARIAN),
    CAREER_CLERIC("water_bucket", MenuType.DATA, Attributes.VillagerCareer.CLERIC),
    CAREER_ARMORER("iron_chestplate", MenuType.DATA, Attributes.VillagerCareer.ARMORER),
    CAREER_WEAPON_SMITH("iron_axe", MenuType.DATA, Attributes.VillagerCareer.WEAPON_SMITH),
    CAREER_TOOL_SMITH("iron_pickaxe", MenuType.DATA, Attributes.VillagerCareer.TOOL_SMITH),
    CAREER_BUTCHER("cooked_beef", MenuType.DATA, Attributes.VillagerCareer.BUTCHER),
    CAREER_LEATHERWORKER("leather", MenuType.DATA, Attributes.VillagerCareer.LEATHERWORKER),

    /*
     * Skeleton types
     */
    SKELETON_TYPE("bone", MenuType.DATA, AttributeType.SKELETON_TYPE),
    SKELETON_TYPE_NORMAL("bow", MenuType.DATA, Attributes.SkeletonType.NORMAL),
    SKELETON_TYPE_WITHER("stone_sword", MenuType.DATA, Attributes.SkeletonType.WITHER),

    /*
     * Slime size
     */
    SIZE("slime_ball", MenuType.DATA, AttributeType.SLIME_SIZE),
    SIZE_SMALL("slime_ball", 1, 0, MenuType.DATA_SECOND_LEVEL, Attributes.SlimeSize.SMALL),
    SIZE_MEDIUM("slime_ball", 2, 0, MenuType.DATA_SECOND_LEVEL, Attributes.SlimeSize.MEDIUM),
    SIZE_LARGE("slime_ball", 3, 0, MenuType.DATA_SECOND_LEVEL, Attributes.SlimeSize.LARGE),
    
    /*
     * Switches
     */
    ANGRY("bone", MenuType.DATA_SECOND_LEVEL, Attributes.Attribute.ANGRY),
    ASLEEP("bed", MenuType.DATA_SECOND_LEVEL, Attributes.Attribute.ASLEEP),
    BABY("wheat", MenuType.DATA_SECOND_LEVEL, Attributes.Attribute.BABY),
    CHESTED("chest", MenuType.DATA_SECOND_LEVEL, Attributes.Attribute.CHESTED),
    FIRE("fire_charge", MenuType.DATA_SECOND_LEVEL, Attributes.Attribute.FIRE),
    IGNITION("flint_and_steel", MenuType.DATA_SECOND_LEVEL, Attributes.Attribute.IGNITION),
    POWER("beacon", MenuType.DATA_SECOND_LEVEL, Attributes.Attribute.POWER),
    ROSE("red_flower", MenuType.DATA_SECOND_LEVEL, Attributes.Attribute.ROSE),
    SADDLE("saddle", MenuType.DATA_SECOND_LEVEL, Attributes.Attribute.SADDLE),
    SCREAMING("ender_pearl", MenuType.DATA_SECOND_LEVEL, Attributes.Attribute.SCREAMING),
    SHEARED("shears", MenuType.DATA_SECOND_LEVEL, Attributes.Attribute.SHEARED),
    SHIELD("glass", MenuType.DATA_SECOND_LEVEL, Attributes.Attribute.SHIELD),
    TAME("bone", MenuType.DATA_SECOND_LEVEL, Attributes.Attribute.TAME),
    VILLAGER("emerald", MenuType.DATA_SECOND_LEVEL, Attributes.Attribute.VILLAGER),
    WITHER("skull", 1, MenuType.DATA_SECOND_LEVEL, Attributes.Attribute.WITHER);

    public static final Icon SELECTOR_PRESET;

    private static final Map<Icon, MenuPreset> ICON_TO_PRESET;
    private static final Map<PetType, List<MenuPreset>> PET_TYPE_PRESETS;
    private static final Map<MenuType, List<MenuPreset>> BY_TYPE;

    static {
        SELECTOR_PRESET = Interact.icon().of(ItemStackContainer.of("bone", 1)).name("Pets").build();

        ICON_TO_PRESET = new HashMap<>();
        for (MenuPreset preset : values()) {
            ICON_TO_PRESET.put(preset.getIcon(), preset);
        }

        PET_TYPE_PRESETS = new HashMap<>();
        for (PetType petType : PetType.values()) {
            List<MenuPreset> presets = new ArrayList<>(Arrays.asList(RIDE, HAT));

            List<EntityAttribute> registeredData = AttributeManager.getModifier(petType).getValidAttributes();
            List<MenuPreset> dataPresets = getPresetsOfType(MenuType.DATA);
            for (MenuPreset preset : dataPresets) {
                if (presets.contains(preset)) {
                    continue;
                }
                for (EntityAttribute attribute : registeredData) {
                    if (attribute.equals(preset.attribute) || attribute.getType().equals(preset.attributeType)) {
                        presets.add(preset);
                    }
                }
            }

            PET_TYPE_PRESETS.put(petType, presets);
        }

        BY_TYPE = new HashMap<>();
        for (MenuType menuType : MenuType.values()) {
            List<MenuPreset> presets = new ArrayList<>();
            for (MenuPreset preset : MenuPreset.values()) {
                if (preset.menuType.equals(menuType)) {
                    presets.add(preset);
                }
            }
            BY_TYPE.put(menuType, presets);
        }
    }

    private String typeId;
    private String name;
    private String command;
    private int quantity;
    private short meta;
    private MenuType menuType;
    private AttributeType attributeType;
    private EntityAttribute attribute;

    MenuPreset(String typeId, MenuType menuType, EntityAttribute attribute) {
        init(typeId, StringUtil.capitalise(toString().replace("_", " ")), null, 0, menuType, AttributeType.SWITCH, attribute);
    }

    MenuPreset(String typeId, int meta, MenuType menuType, EntityAttribute attribute) {
        init(typeId, StringUtil.capitalise(toString().replace("_", " ")), null, meta, menuType, AttributeType.SWITCH, attribute);
    }

    MenuPreset(String typeId, int quantity, int meta, MenuType menuType, EntityAttribute attribute) {
        init(typeId, StringUtil.capitalise(toString().replace("_", " ")), null, quantity, meta, menuType, AttributeType.SWITCH, attribute);
    }

    MenuPreset(String typeId, MenuType menuType, AttributeType attributeType) {
        init(typeId, StringUtil.capitalise(toString().replace("_", " ")), null, 0, menuType, attributeType, null);
    }

    MenuPreset(String typeId, String name, String command, MenuType menuType) {
        init(typeId, name, command, 0, menuType, null, null);
    }

    public static List<MenuPreset> getPresetsOfType(MenuType menuType, AttributeType attributeType) {
        List<MenuPreset> presets = new ArrayList<>();
        for (MenuPreset preset : MenuPreset.values()) {
            if ((menuType == null || menuType.equals(preset.menuType)) && (preset.attributeType != null && preset.attributeType.equals(attributeType))) {
                presets.add(preset);
            }
        }
        return presets;
    }
    
    public static List<MenuPreset> getPresetsOfType(AttributeType attributeType) {
        return getPresetsOfType(null, attributeType);
    }

    public static List<MenuPreset> getPresetsOfType(MenuType type) {
        return Collections.unmodifiableList(BY_TYPE.get(type));
    }

    public static List<MenuPreset> getPresets(PetType petType) {
        return Collections.unmodifiableList(PET_TYPE_PRESETS.get(petType));
    }

    public static Map<Icon, MenuPreset> getIconToTypeMap() {
        return Collections.unmodifiableMap(ICON_TO_PRESET);
    }

    private void init(String typeId, String name, String command, int materialData, MenuType menuType, AttributeType attributeType, EntityAttribute attribute) {
        init(typeId, name, command, 1, materialData, menuType, attributeType, attribute);
    }

    private void init(String typeId, String name, String command, int quantity, int meta, MenuType menuType, AttributeType attributeType, EntityAttribute attribute) {
        this.typeId = typeId;
        this.name = name;
        this.command = "pet " + command;
        this.quantity = quantity;
        this.meta = (short) meta;
        this.menuType = menuType;
        this.attributeType = attributeType;
        this.attribute = attribute;
    }

    public String getCommand() {
        return command;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getName() {
        return name;
    }

    public short getTypeMeta() {
        return meta;
    }

    public int getQuantity() {
        return quantity;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public AttributeType getAttributeType() {
        return attributeType;
    }

    public EntityAttribute getAttribute() {
        return attribute;
    }

    public Icon getIcon() {
        Icon.Builder builder;
        if (command != null) {
            builder = Interact.commandIcon().command(command);
        } else {
            builder = Interact.icon();
        }
        return builder.name(name).of(ItemStackContainer.of(typeId, meta, quantity)).build();
    }

    public enum MenuType {
        SELECTOR, DATA, DATA_SECOND_LEVEL
    }
}