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

package com.dsh105.echopet.api.entity.attribute;

import com.dsh105.echopet.util.StringForm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum AttributeType {

    CAREER(Attributes.VillagerCareer.class),
    COLOR(Attributes.Color.class),
    HORSE_ARMOUR(Attributes.HorseArmour.class),
    HORSE_COLOUR(Attributes.HorseColor.class),
    HORSE_STYLE(Attributes.HorseStyle.class),
    HORSE_VARIANT(Attributes.HorseVariant.class),
    OCELOT_TYPE(Attributes.OcelotType.class),
    PROFESSION(Attributes.VillagerProfession.class),
    SLIME_SIZE(Attributes.SlimeSize.class),
    SKELETON_TYPE(Attributes.SkeletonType.class),
    SWITCH;

    private Class<?> attributeClass;
    private final List<AttributeValue> validValues;

    AttributeType() {
        this(Attributes.Attribute.class);
    }

    AttributeType(Class<? extends EntityAttribute> attributeClass) {
        // enum or enum bridge type
        List<AttributeValue> values = new ArrayList<>();
        if (attributeClass.isEnum()) {
            for (EntityAttribute enumConstant : attributeClass.getEnumConstants()) {
                values.add(new AttributeValue(enumConstant));
            }
        } else if (AttributeEnumBridge.class.isAssignableFrom(attributeClass)) {
            Class<? extends AttributeEnumBridge> enumBridge = (Class<? extends AttributeEnumBridge>) attributeClass;
            for (AttributeEnumBridge bridgeValue : Attributes.values(enumBridge)) {
                values.add(new AttributeValue(bridgeValue));
            }
        } else {
            throw new IllegalArgumentException("Attribute type class must be either an enum or AttributeEnumBridge.");
        }
        validValues = Collections.unmodifiableList(values);
    }

    public String getConfigName() {
        return StringForm.create(this).name();
    }

    public List<AttributeValue> getValidValues() {
        return validValues;
    }

    public AttributeValue getValue(EntityAttribute attribute) {
        for (AttributeValue attributeValue : validValues) {
            if (attributeValue.getValue().equals(attribute)) {
                return attributeValue;
            }
        }
        return null;
    }

    public static AttributeType getByEnumBridge(Class<?> attributeClass) {
        if (!valid(attributeClass)) {
            throw new IllegalArgumentException("Attribute type class must be either an enum or AttributeEnumBridge.");
        }
        for (AttributeType attributeType : values()) {
            if (attributeType.attributeClass == attributeClass) {
                return attributeType;
            }
        }
        return null;
    }

    private static boolean valid(Class<?> attributeClass) {
        return AttributeEnumBridge.class.isAssignableFrom(attributeClass) || attributeClass.isEnum();
    }
}