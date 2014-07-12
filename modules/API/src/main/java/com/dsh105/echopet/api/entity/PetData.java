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

package com.dsh105.echopet.api.entity;

import com.google.common.collect.ImmutableList;
import org.bukkit.DyeColor;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Villager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public enum PetData {

    ANGRY("angry", Type.BOOLEAN),
    BABY("baby", Type.BOOLEAN),
    BLACK("black", Type.COLOUR, Type.CAT_TYPE, Type.HORSE_VARIANT),
    BLACKSMITH("blacksmith", Type.VILLAGER_PROFESSION),
    BLACKSPOT("blackSpot", Type.HORSE_MARKING),
    BLUE("blue", Type.COLOUR),
    BROWN("brown", Type.COLOUR, Type.HORSE_VARIANT),
    BUTCHER("butcher", Type.VILLAGER_PROFESSION),
    CHESTED("chested", Type.BOOLEAN),
    CHESTNUT("chestnut", Type.HORSE_VARIANT),
    CREAMY("creamy", Type.HORSE_VARIANT),
    CYAN("cyan", Type.COLOUR),
    DARKBROWN("darkbrown", Type.HORSE_VARIANT),
    DIAMOND("diamond", Type.HORSE_ARMOUR),
    DONKEY("donkey", Type.HORSE_TYPE),
    FARMER("farmer", Type.VILLAGER_PROFESSION),
    FIRE("fire", Type.BOOLEAN),
    GRAY("gray", Type.COLOUR, Type.HORSE_VARIANT),
    GREEN("green", Type.COLOUR),
    GOLD("gold", Type.HORSE_ARMOUR),
    IRON("iron", Type.HORSE_ARMOUR),
    LARGE("large", Type.SLIME_SIZE),
    LIBRARIAN("librarian", Type.VILLAGER_PROFESSION),
    LIGHTBLUE("lightBlue", Type.COLOUR),
    LIME("lime", Type.COLOUR),
    MAGENTA("magenta", Type.COLOUR),
    MEDIUM("medium", Type.SLIME_SIZE),
    MULE("mule", Type.HORSE_TYPE),
    NOARMOUR("noarmour", Type.HORSE_ARMOUR),
    NONE("noMarking", Type.HORSE_MARKING),
    NORMAL("normal", Type.HORSE_TYPE),
    ORANGE("orange", Type.COLOUR),
    PINK("pink", Type.COLOUR),
    POWER("powered", Type.BOOLEAN),
    PRIEST("priest", Type.VILLAGER_PROFESSION),
    PURPLE("purple", Type.COLOUR),
    RED("red", Type.CAT_TYPE, Type.COLOUR),
    SADDLE("saddle", Type.BOOLEAN),
    SCREAMING("screaming", Type.BOOLEAN),
    SHEARED("sheared", Type.BOOLEAN),
    SHIELD("shield", Type.BOOLEAN),
    SIAMESE("siamese", Type.CAT_TYPE),
    SILVER("silver", Type.COLOUR),
    SKELETON("skeleton", Type.HORSE_TYPE),
    SMALL("small", Type.SLIME_SIZE),
    SOCKS("whiteSocks", Type.HORSE_MARKING),
    TAMED("tamed", Type.BOOLEAN),
    VILLAGER("villager", Type.BOOLEAN),
    WHITEPATCH("whitePatch", Type.HORSE_MARKING),
    WHITESPOT("whiteSpot", Type.HORSE_MARKING),
    WHITE("white", Type.COLOUR, Type.HORSE_VARIANT),
    WILD("wild", Type.CAT_TYPE),
    WITHER("wither", Type.BOOLEAN),
    YELLOW("yellow", Type.COLOUR),
    ZOMBIE("zombie", Type.HORSE_TYPE);


    private String storageName;
    private List<Type> validTypes;

    private HashMap<Type, Object> typeToObjectMap = new HashMap<Type, Object>();

    PetData(String storageName, Type... validTypes) {
        this.storageName = storageName;
        this.validTypes = ImmutableList.copyOf(validTypes);
        setup();
    }

    private void setup() {
        for (Type type : validTypes) {
            if (type == PetData.Type.SLIME_SIZE) {
                typeToObjectMap.put(type, this == LARGE ? 4 : (this == PetData.MEDIUM ? 2 : 1));
            } else if (type == PetData.Type.CAT_TYPE) {
                typeToObjectMap.put(type, Ocelot.Type.valueOf(this.toString() + (this == PetData.WILD ? "_OCELOT" : "_CAT")));
            } else if (type == PetData.Type.VILLAGER_PROFESSION) {
                typeToObjectMap.put(type, Villager.Profession.valueOf(this.toString()));
            } else if (type == PetData.Type.COLOUR) {
                typeToObjectMap.put(type, DyeColor.valueOf(this == PetData.LIGHTBLUE ? "LIGHT_BLUE" : this.toString()));
            } else if (type == PetData.Type.HORSE_TYPE) {
                typeToObjectMap.put(type, HorseType.valueOf(this.toString()));
            } else if (type == PetData.Type.HORSE_VARIANT) {
                typeToObjectMap.put(type, HorseVariant.valueOf(this.toString()));
            } else if (type == PetData.Type.HORSE_MARKING) {
                typeToObjectMap.put(type, this == PetData.WHITEPATCH ? HorseMarking.WHITE_SPOTS : (this == PetData.WHITESPOT ? HorseMarking.WHITE_SPOTS : (this == PetData.BLACKSPOT ? HorseMarking.BLACK_SPOTS : HorseMarking.valueOf(this.toString()))));
            } else if (type == PetData.Type.HORSE_ARMOUR) {
                typeToObjectMap.put(type, this == PetData.NOARMOUR ? HorseArmour.NONE : HorseArmour.valueOf(this.toString()));
            }
        }
    }

    public HashMap<Type, Object> getTypeToObjectMap() {
        return typeToObjectMap;
    }

    public String storageName() {
        return this.storageName;
    }

    public List<Type> getTypes() {
        return this.validTypes;
    }

    public boolean isType(Type t) {
        return this.validTypes.contains(t);
    }

    public static ArrayList<PetData> allOfType(Type type) {
        ArrayList<PetData> dataOfType = new ArrayList<PetData>();
        for (PetData data : PetData.values()) {
            if (data.isType(type)) {
                dataOfType.add(data);
            }
        }
        return dataOfType;
    }

    public enum Type {
        BOOLEAN, COLOUR, CAT_TYPE, SLIME_SIZE, VILLAGER_PROFESSION, HORSE_TYPE, HORSE_VARIANT, HORSE_MARKING, HORSE_ARMOUR
    }
}