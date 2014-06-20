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

package com.dsh105.echopet.reflection;

import com.dsh105.commodus.logging.Level;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassTemplate<T> {

    private Class<T> type;
    private List<SafeField<?>> fields;

    public ClassTemplate() {
    }

    public ClassTemplate(Class<T> clazz) {
        setClass(clazz);
    }

    protected void setClass(Class<T> clazz) {
        this.type = clazz;
    }

    public List<SafeField<?>> getFields() {
        if (type == null) {
            return Collections.emptyList();
        }
        if (fields == null) {
            fields = populateFieldList(new ArrayList<SafeField<?>>(), type);
        }
        return Collections.unmodifiableList(fields);
    }

    private static List<SafeField<?>> populateFieldList(List<SafeField<?>> fields, Class<?> clazz) {
        if (clazz == null) {
            return fields;
        }
        Field[] declared = clazz.getDeclaredFields();
        ArrayList<SafeField<?>> newFields = new ArrayList<SafeField<?>>(declared.length);
        for (Field field : declared) {
            if (!Modifier.isStatic(field.getModifiers())) {
                newFields.add(new SafeField<Object>(field));
            }
        }
        fields.addAll(0, newFields);
        return populateFieldList(fields, clazz.getSuperclass());
    }

    public T newInstance() {
        if (this.type == null) {
            throw new IllegalStateException("Class not set.");
        }

        try {
            return getType().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class<T> getType() {
        return this.type;
    }

    public static ClassTemplate<?> create(Class<?> type) {
        if (type == null) {
            EchoPet.LOG.console(Level.WARNING, "Cannot create a ClassTemplate with a null type!");
            return null;
        }
        return new ClassTemplate(type);
    }

    public static ClassTemplate<?> create(String className) {
        Class clazz = ReflectionUtil.getClass(className);

        if (clazz == null) {
            EchoPet.LOG.console(Level.WARNING, "Failed to find a matching class with name: " + className);
            return null;
        }
        return new ClassTemplate<Object>(clazz);
    }

    public void transfer(Object from, Object to) {
        for (FieldAccessor<?> field : this.getFields()) {
            field.transfer(from, to);
        }
    }

    public boolean isAssignableFrom(Class<?> clazz) {
        return this.getType().isAssignableFrom(clazz);
    }

    public boolean isInstanceOf(Object object) {
        return this.getType().isInstance(object);
    }

    public <K> MethodAccessor<K> getMethod(String methodname, Class<?>... params) {
        return new SafeMethod<K>(this.getType(), methodname, params);
    }

    public <K> FieldAccessor<K> getField(String fieldName) {
        return new SafeField<K>(getType(), fieldName);
    }

    public <K> SafeConstructor<K> getConstructor(Class<?>... params) {
        return new SafeConstructor<K>(getType(), params);
    }

    public <K> K getStaticFieldValue(String name) {
        return SafeField.get(getType(), name);
    }

    public <K> void setStaticFieldValue(String name, K value) {
        SafeField.setStatic(getType(), name, value);
    }
}
