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

package com.dsh105.echopetv3.api.entity;

import com.dsh105.commodus.StringUtil;
import com.dsh105.echopetv3.api.entity.pet.HorseArmour;
import com.google.common.collect.ImmutableList;
import org.bukkit.DyeColor;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Villager;

import java.util.*;

public enum PetData {

    ANGRY("angry", Type.BOOLEAN),
    BABY("baby", Type.BOOLEAN),
    BLACK("black", Type.COLOUR, Type.CAT_TYPE, Type.HORSE_COLOUR),
    BLACKSMITH("blacksmith", Type.VILLAGER_PROFESSION),
    BLACK_DOTS("blackDots", Type.HORSE_STYLE),
    BLUE("blue", Type.COLOUR),
    BROWN("brown", Type.COLOUR, Type.HORSE_COLOUR),
    BUTCHER("butcher", Type.VILLAGER_PROFESSION),
    CHESTED("chested", Type.BOOLEAN),
    CHESTNUT("chestnut", Type.HORSE_COLOUR),
    CREAMY("creamy", Type.HORSE_COLOUR),
    CYAN("cyan", Type.COLOUR),
    DARK_BROWN("darkbrown", Type.HORSE_COLOUR),
    DIAMOND("diamond", Type.HORSE_ARMOUR),
    DONKEY("donkey", Type.VARIANT),
    FARMER("farmer", Type.VILLAGER_PROFESSION),
    FIRE("fire", Type.BOOLEAN),
    GRAY("gray", Type.COLOUR, Type.HORSE_COLOUR),
    GREEN("green", Type.COLOUR),
    GOLD("gold", Type.HORSE_ARMOUR),
    IRON("iron", Type.HORSE_ARMOUR),
    LARGE("large", Type.SLIME_SIZE),
    LIBRARIAN("librarian", Type.VILLAGER_PROFESSION),
    LIGHT_BLUE("lightBlue", Type.COLOUR),
    LIME("lime", Type.COLOUR),
    MAGENTA("magenta", Type.COLOUR),
    MEDIUM("medium", Type.SLIME_SIZE),
    MULE("mule", Type.VARIANT),
    NO_ARMOUR("noarmour", Type.HORSE_ARMOUR),
    NONE("noMarking", Type.HORSE_STYLE),
    HORSE("normal", Type.VARIANT),
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
    SKELETON_HORSE("skeleton", Type.VARIANT),
    SMALL("small", Type.SLIME_SIZE),
    SOCKS("whiteSocks", Type.HORSE_STYLE),
    TAMED("tamed", Type.BOOLEAN),
    VILLAGER("villager", Type.BOOLEAN),
    WHITEFIELD("whitefield", Type.HORSE_STYLE),
    WHITE_DOTS("whiteDots", Type.HORSE_STYLE),
    WHITE("white", Type.COLOUR, Type.HORSE_COLOUR),
    WILD("wild", Type.CAT_TYPE),
    WITHER("wither", Type.BOOLEAN),
    YELLOW("yellow", Type.COLOUR),
    ZOMBIE("zombie", Type.VARIANT),

    DEFAULT(); // hax

    private static PetData[] VALUES;

    static {
        List<PetData> valid = Arrays.asList(PetData.values());
        valid.remove(PetData.DEFAULT);
        VALUES = valid.toArray(new PetData[0]);
    }

    private String storageName;
    private List<Type> validTypes;
    private HashMap<Type, Object> typeToObjectMap = new HashMap<>();

    PetData() {

    }

    PetData(String storageName, Type... validTypes) {
        this.storageName = storageName;
        this.validTypes = ImmutableList.copyOf(validTypes);

        // Setup any default values (etc.) for each data type
        for (Type type : validTypes) {
            type.setup(this);
        }
    }

    public Map<Type, Object> getTypeToObjectMap() {
        return Collections.unmodifiableMap(typeToObjectMap);
    }

    public String storageName() {
        return this.storageName;
    }

    public String humanName() {
        return StringUtil.capitalise(name().replace("_", " "));
    }

    public List<Type> getTypes() {
        return this.validTypes;
    }

    public boolean isType(Type t) {
        return this.validTypes.contains(t);
    }

    public static ArrayList<PetData> allOfType(Type type) {
        ArrayList<PetData> dataOfType = new ArrayList<>();
        for (PetData data : PetData.valid()) {
            if (data.isType(type)) {
                dataOfType.add(data);
            }
        }
        return dataOfType;
    }

    // This isn't pretty, but it can allow us to workaround other things
    public static PetData[] valid() {
        return VALUES;
    }

    public enum Type {
        BOOLEAN,

        COLOUR {
            @Override
            public void setup(PetData petData) {
                petData.typeToObjectMap.put(this, DyeColor.valueOf(petData.toString()));
            }
        },

        CAT_TYPE {
            @Override
            public void setup(PetData petData) {
                petData.typeToObjectMap.put(this, Ocelot.Type.valueOf(petData.toString() + (petData == PetData.WILD ? "_OCELOT" : "_CAT")));
            }
        },

        SLIME_SIZE {
            @Override
            public void setup(PetData petData) {
                petData.typeToObjectMap.put(this, petData == LARGE ? 4 : (petData == PetData.MEDIUM ? 2 : 1));
            }
        },
        VILLAGER_PROFESSION {
            @Override
            public void setup(PetData petData) {
                petData.typeToObjectMap.put(this, Villager.Profession.valueOf(petData.toString()));
            }
        },

        VARIANT {
            @Override
            public void setup(PetData petData) {
                petData.typeToObjectMap.put(this, Horse.Variant.valueOf(petData.name()));
            }
        },

        HORSE_COLOUR {
            @Override
            public void setup(PetData petData) {
                petData.typeToObjectMap.put(this, Horse.Color.valueOf(petData.name()));
            }
        },

        HORSE_STYLE {
            @Override
            public void setup(PetData petData) {
                petData.typeToObjectMap.put(this, petData == SOCKS ? Horse.Style.WHITE : Horse.Style.valueOf(petData.name()));
            }
        },

        HORSE_ARMOUR {
            @Override
            public void setup(PetData petData) {
                petData.typeToObjectMap.put(this, petData == PetData.NO_ARMOUR ? HorseArmour.NONE : HorseArmour.valueOf(petData.toString()));
            }
        };

        protected void setup(PetData petData) {
            // do nothing
        }
    }
}