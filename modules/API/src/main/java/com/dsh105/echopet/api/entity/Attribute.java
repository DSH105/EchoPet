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

import java.lang.reflect.Method;

public class Attribute {

    private Method method;
    private boolean getter;
    private PetData data;
    private PetData.Type dataType;

    protected Attribute(AttributeHandler attributeHandler, Method method) {
        this.method = method;
        this.getter = attributeHandler.getter();
        this.data = attributeHandler.data();
        this.dataType = attributeHandler.dataType();
    }

    public Method getMethod() {
        return method;
    }

    public boolean isGetter() {
        return getter;
    }

    public PetData getData() {
        return data;
    }

    public PetData.Type getDataType() {
        return dataType;
    }
}