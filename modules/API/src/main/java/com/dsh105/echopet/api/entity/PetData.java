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

import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.api.entity.attribute.Attributes;
import com.dsh105.echopet.api.entity.attribute.EntityAttribute;
import com.dsh105.echopet.api.entity.pet.HorseArmour;
import com.google.common.collect.ImmutableList;

import java.util.*;

@Deprecated // only here for legacy support
public enum PetData {

    /*
     * Some of these don't really 'convert' to attributes as they cover more than one type of attribute
     */

    ANGRY(Attributes.Attribute.ANGRY, "angry", Type.BOOLEAN),
    BABY(Attributes.Attribute.BABY, "baby", Type.BOOLEAN),
    BLACK("black", Type.COLOUR, Type.CAT_TYPE, Type.HORSE_COLOUR),
    BLACKSMITH(Attributes.VillagerProfession.BLACKSMITH, "blacksmith", Type.VILLAGER_PROFESSION),
    BLACK_DOTS(Attributes.HorseStyle.BLACK_DOTS, "blackDots", Type.HORSE_STYLE),
    BLUE(Attributes.Color.BLUE, "blue", Type.COLOUR),
    BROWN(Attributes.Color.BROWN, "brown", Type.COLOUR, Type.HORSE_COLOUR),
    BUTCHER(Attributes.VillagerProfession.BUTCHER, "butcher", Type.VILLAGER_PROFESSION),
    CHESTED(Attributes.Attribute.CHESTED, "chested", Type.BOOLEAN),
    CHESTNUT(Attributes.HorseColor.CHESTNUT, "chestnut", Type.HORSE_COLOUR),
    CREAMY(Attributes.HorseColor.CREAMY, "creamy", Type.HORSE_COLOUR),
    CYAN(Attributes.Color.CYAN, "cyan", Type.COLOUR),
    DARK_BROWN(Attributes.HorseColor.DARK_BROWN, "darkbrown", Type.HORSE_COLOUR),
    DIAMOND(Attributes.HorseArmour.DIAMOND, "diamond", Type.HORSE_ARMOUR),
    DONKEY(Attributes.HorseVariant.DONKEY, "donkey", Type.VARIANT),
    FARMER(Attributes.VillagerProfession.FARMER, "farmer", Type.VILLAGER_PROFESSION),
    FIRE(Attributes.Attribute.FIRE, "fire", Type.BOOLEAN),
    GRAY("gray", Type.COLOUR, Type.HORSE_COLOUR),
    GREEN(Attributes.Color.GREEN, "green", Type.COLOUR),
    GOLD(Attributes.HorseArmour.GOLD, "gold", Type.HORSE_ARMOUR),
    IGNITED(Attributes.Attribute.IGNITION, "ignited", Type.BOOLEAN),
    IRON(Attributes.HorseArmour.IRON, "iron", Type.HORSE_ARMOUR),
    LARGE(Attributes.SlimeSize.LARGE, "large", Type.SLIME_SIZE),
    LIBRARIAN(Attributes.VillagerProfession.LIBRARIAN, "librarian", Type.VILLAGER_PROFESSION),
    LIGHT_BLUE(Attributes.Color.LIGHT_BLUE, "lightBlue", Type.COLOUR),
    LIME(Attributes.Color.LIME, "lime", Type.COLOUR),
    MAGENTA(Attributes.Color.MAGENTA, "magenta", Type.COLOUR),
    MEDIUM(Attributes.SlimeSize.MEDIUM, "medium", Type.SLIME_SIZE),
    MULE(Attributes.HorseVariant.MULE, "mule", Type.VARIANT),
    NO_ARMOUR(Attributes.HorseArmour.NONE, "noarmour", Type.HORSE_ARMOUR),
    NONE(Attributes.HorseStyle.NONE, "noMarking", Type.HORSE_STYLE),
    HORSE(Attributes.HorseVariant.NORMAL, "normal", Type.VARIANT),
    ORANGE(Attributes.Color.ORANGE, "orange", Type.COLOUR),
    PINK(Attributes.Color.PINK, "pink", Type.COLOUR),
    POWER(Attributes.Attribute.POWER, "powered", Type.BOOLEAN),
    PRIEST(Attributes.VillagerProfession.PRIEST, "priest", Type.VILLAGER_PROFESSION),
    PURPLE(Attributes.Color.PURPLE, "purple", Type.COLOUR),
    RED("red", Type.CAT_TYPE, Type.COLOUR),
    ROSE(Attributes.Attribute.ROSE, "rose", Type.BOOLEAN),
    SADDLE(Attributes.Attribute.SADDLE, "saddle", Type.BOOLEAN),
    SCREAMING(Attributes.Attribute.SCREAMING, "screaming", Type.BOOLEAN),
    SHEARED(Attributes.Attribute.SHEARED, "sheared", Type.BOOLEAN),
    SHIELD(Attributes.Attribute.SHIELD, "shield", Type.BOOLEAN),
    SIAMESE(Attributes.OcelotType.SIAMESE, "siamese", Type.CAT_TYPE),
    SILVER(Attributes.Color.SILVER, "silver", Type.COLOUR),
    SKELETON_HORSE(Attributes.HorseVariant.SKELETON, "skeleton", Type.VARIANT),
    SMALL(Attributes.SlimeSize.SMALL, "small", Type.SLIME_SIZE),
    SOCKS(Attributes.HorseStyle.WHITE, "whiteSocks", Type.HORSE_STYLE),
    TAMED(Attributes.Attribute.TAME, "tamed", Type.BOOLEAN),
    VILLAGER(Attributes.Attribute.VILLAGER, "villager", Type.BOOLEAN),
    WHITEFIELD(Attributes.HorseStyle.WHITEFIELD, "whitefield", Type.HORSE_STYLE),
    WHITE_DOTS(Attributes.HorseStyle.WHITE_DOTS, "whiteDots", Type.HORSE_STYLE),
    WHITE("white", Type.COLOUR, Type.HORSE_COLOUR),
    WILD(Attributes.OcelotType.WILD, "wild", Type.CAT_TYPE),
    WITHER(Attributes.Attribute.WITHER, "wither", Type.BOOLEAN),
    YELLOW(Attributes.Color.YELLOW, "yellow", Type.COLOUR),
    ZOMBIE(Attributes.HorseVariant.ZOMBIE, "zombie", Type.VARIANT),

    DEFAULT; // hax

    private static PetData[] VALUES;

    static {
        ArrayList<PetData> valid = new ArrayList<>(Arrays.asList(PetData.values()));
        valid.remove(PetData.DEFAULT);
        VALUES = valid.toArray(new PetData[0]);
    }

    private EntityAttribute attribute;
    private String storageName;
    private List<Type> validTypes;
    private HashMap<Type, Object> typeToObjectMap;

    PetData() {

    }

    PetData(String storageName, Type... validTypes) {
        this(null, storageName, validTypes);
    }

    PetData(EntityAttribute attribute, String storageName, Type... validTypes) {
        this.attribute = attribute;
        this.storageName = storageName;
        this.validTypes = ImmutableList.copyOf(validTypes);
    }

    public static List<PetData> allOfType(Type type) {
        List<PetData> dataOfType = new ArrayList<>();
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

    public EntityAttribute getCorrespondingAttribute() {
        return attribute;
    }

    public Map<Type, Object> getTypeToObjectMap() {
        if (typeToObjectMap == null) {
            typeToObjectMap = new HashMap<>();

            // Setup any default values (etc.) for each data type
            for (Type type : getTypes()) {
                type.setup(this);
            }
        }
        return Collections.unmodifiableMap(typeToObjectMap);
    }

    public String storageName() {
        return this.storageName;
    }

    public String humanName() {
        return StringUtil.capitalise(name().replace("_", " "));
    }

    public List<Type> getTypes() {
        return Collections.unmodifiableList(this.validTypes);
    }

    public boolean isType(Type t) {
        return this.getTypes().contains(t);
    }

    public enum Type {
        BOOLEAN,

        COLOUR {
            @Override
            public void setup(PetData petData) {
                petData.typeToObjectMap.put(this, Attributes.Color.valueOf(petData.toString()));
            }
        },

        CAT_TYPE {
            @Override
            public void setup(PetData petData) {
                petData.typeToObjectMap.put(this, Attributes.OcelotType.valueOf(petData.toString() + (petData == PetData.WILD ? "_OCELOT" : "_CAT")));
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
                petData.typeToObjectMap.put(this, Attributes.VillagerProfession.valueOf(petData.toString()));
            }
        },

        VARIANT {
            @Override
            public void setup(PetData petData) {
                petData.typeToObjectMap.put(this, (petData == SKELETON_HORSE ? Attributes.HorseVariant.SKELETON : Attributes.HorseVariant.valueOf(petData.name())));
            }
        },

        HORSE_COLOUR {
            @Override
            public void setup(PetData petData) {
                petData.typeToObjectMap.put(this, Attributes.HorseColor.valueOf(petData.name()));
            }
        },

        HORSE_STYLE {
            @Override
            public void setup(PetData petData) {
                petData.typeToObjectMap.put(this, petData == SOCKS ? Attributes.HorseStyle.WHITE : Attributes.HorseStyle.valueOf(petData.name()));
            }
        },

        HORSE_ARMOUR {
            @Override
            public void setup(PetData petData) {
                petData.typeToObjectMap.put(this, petData == NO_ARMOUR ? HorseArmour.NONE : HorseArmour.valueOf(petData.toString()));
            }
        };

        protected void setup(PetData petData) {
            // do nothing
        }
    }
}