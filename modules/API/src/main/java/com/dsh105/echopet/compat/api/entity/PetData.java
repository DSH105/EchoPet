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

package com.dsh105.echopet.compat.api.entity;

import com.google.common.collect.ImmutableList;

import java.util.List;

public enum PetData {

    ANGRY("angry", Type.BOOLEAN),
    BABY("baby", Type.BOOLEAN),
    BLACK("black", Type.COLOUR, Type.CAT, Type.HORSE_VARIANT, Type.RABBIT_TYPE),
    BLACK_AND_WHITE("black_and_white", Type.RABBIT_TYPE),
    BLACKSMITH("blacksmith", Type.PROF),
    BLACKSPOT("blackSpot", Type.HORSE_MARKING),
    BLUE("blue", Type.COLOUR),
    BROWN("brown", Type.COLOUR, Type.HORSE_VARIANT, Type.RABBIT_TYPE),
    BUTCHER("butcher", Type.PROF),
    CHESTED("chested", Type.BOOLEAN),
    CHESTNUT("chestnut", Type.HORSE_VARIANT),
    CREAMY("creamy", Type.HORSE_VARIANT),
    CYAN("cyan", Type.COLOUR),
    DARKBROWN("darkbrown", Type.HORSE_VARIANT),
    DIAMOND("diamond", Type.HORSE_ARMOUR),
    DONKEY("donkey", Type.HORSE_TYPE),
    ELDER("elder", Type.BOOLEAN),
    FARMER("farmer", Type.PROF),
    FIRE("fire", Type.BOOLEAN),
    GRAY("gray", Type.COLOUR, Type.HORSE_VARIANT),
    GREEN("green", Type.COLOUR),
    GOLD("gold", Type.HORSE_ARMOUR),
    IRON("iron", Type.HORSE_ARMOUR),
    THE_KILLER_BUNNY("killer_bunny", Type.RABBIT_TYPE),
    LARGE("large", Type.SIZE),
    LIBRARIAN("librarian", Type.PROF),
    LIGHTBLUE("lightBlue", Type.COLOUR),
    LIME("lime", Type.COLOUR),
    MAGENTA("magenta", Type.COLOUR),
    MEDIUM("medium", Type.SIZE),
    MULE("mule", Type.HORSE_TYPE),
    NOARMOUR("noarmour", Type.HORSE_ARMOUR),
    NONE("noMarking", Type.HORSE_MARKING),
    NORMAL("normal", Type.HORSE_TYPE),
    ORANGE("orange", Type.COLOUR),
    PINK("pink", Type.COLOUR),
    POWER("powered", Type.BOOLEAN),
    PRIEST("priest", Type.PROF),
    PURPLE("purple", Type.COLOUR),
    RED("red", Type.CAT, Type.COLOUR),
    SADDLE("saddle", Type.BOOLEAN),
    SALT_AND_PEPPER("salt_and_pepper", Type.RABBIT_TYPE),
    SCREAMING("screaming", Type.BOOLEAN),
    SHEARED("sheared", Type.BOOLEAN),
    SHIELD("shield", Type.BOOLEAN),
    SIAMESE("siamese", Type.CAT),
    SILVER("silver", Type.COLOUR),
    SKELETON("skeleton", Type.HORSE_TYPE),
    SMALL("small", Type.SIZE),
    SOCKS("whiteSocks", Type.HORSE_MARKING),
    TAMED("tamed", Type.BOOLEAN),
    VILLAGER("villager", Type.BOOLEAN),
    WHITEPATCH("whitePatch", Type.HORSE_MARKING),
    WHITESPOT("whiteSpot", Type.HORSE_MARKING),
    WHITE("white", Type.COLOUR, Type.HORSE_VARIANT, Type.RABBIT_TYPE),
    WILD("wild", Type.CAT),
    WITHER("wither", Type.BOOLEAN),
    YELLOW("yellow", Type.COLOUR),
    ZOMBIE("zombie", Type.HORSE_TYPE);


    private String configOptionString;
    private List<Type> t;

    PetData(String configOptionString, Type... t) {
        this.configOptionString = configOptionString;
        this.t = ImmutableList.copyOf(t);
    }

    public String getConfigOptionString() {
        return this.configOptionString;
    }

    public List<Type> getTypes() {
        return this.t;
    }

    public boolean isType(Type t) {
        return this.t.contains(t);
    }

    public enum Type {
        BOOLEAN, COLOUR, CAT, SIZE, PROF, HORSE_TYPE, HORSE_VARIANT, HORSE_MARKING, HORSE_ARMOUR, RABBIT_TYPE
    }
}