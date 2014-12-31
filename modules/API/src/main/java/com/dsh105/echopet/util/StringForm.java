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

package com.dsh105.echopet.util;

import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.api.entity.attribute.AttributeEnumBridge;
import com.dsh105.echopet.api.entity.attribute.AttributeValue;
import com.dsh105.echopet.api.entity.attribute.EntityAttribute;

import java.util.HashMap;
import java.util.Map;

public class StringForm {

    private static Map<String, StringForm> FORMS = new HashMap<>();

    private String name;
    private String capitalisedName;
    private String configName;

    protected StringForm(String name) {
        this.name = name;
        this.capitalisedName = StringUtil.capitalise(name().replace("_", " "));
        this.configName = (name.substring(0, 1) + name.substring(2)).replaceAll("\\s", "");
    }

    public static StringForm create(EntityAttribute entityAttribute) {
        String name;
        if (entityAttribute instanceof AttributeEnumBridge) {
            name = ((AttributeEnumBridge) entityAttribute).name();
        } else if (entityAttribute instanceof Enum) {
            name = ((Enum) entityAttribute).name();
        } else {
            // should never happen
            throw new IllegalStateException("Illegal attribute class type provided.");
        }
        return create(name);
    }

    public static StringForm create(Enum<?> enumType) {
        return create(enumType.name());
    }

    public static StringForm create(AttributeValue attributeValue) {
        return create(attributeValue.name());
    }

    public static StringForm create(String name) {
        StringForm stringForm = FORMS.get(name);
        if (stringForm == null) {
            stringForm = new StringForm(name);
            FORMS.put(name, stringForm);
        }
        return stringForm;
    }

    public String name() {
        return name;
    }

    public String getCaptalisedName() {
        return capitalisedName;
    }

    public String getConfigName() {
        return configName;
    }
}