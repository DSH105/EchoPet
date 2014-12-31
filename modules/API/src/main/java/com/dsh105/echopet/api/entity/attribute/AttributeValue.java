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

public class AttributeValue {

    private EntityAttribute value;

    public AttributeValue(EntityAttribute value) {
        this.value = value;
    }

    public EntityAttribute getValue() {
        return value;
    }

    public int ordinal() {
        if (value instanceof AttributeEnumBridge) {
            return ((AttributeEnumBridge) value).ordinal();
        } else if (value instanceof Enum) {
            return ((Enum) value).ordinal();
        }
        // should never happen
        throw new IllegalStateException("Illegal attribute class type provided.");
    }

    public String name() {
        if (value instanceof AttributeEnumBridge) {
            return ((AttributeEnumBridge) value).name();
        } else if (value instanceof Enum) {
            return ((Enum) value).name();
        }
        // should never happen
        throw new IllegalStateException("Illegal attribute class type provided.");
    }

    public String getConfigName() {
        return StringForm.create(this).getConfigName();
    }
}