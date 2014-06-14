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

/*
 * This file is part of HoloAPI.
 *
 * HoloAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HoloAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HoloAPI.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.reflection.utility;

/**
 * Handles the loading/finding of specific classes. This is mainly to be compatible with third and fourth party mods
 * like MCPC+
 */
public abstract class ClassHandler {

    protected ClassLoader classLoader;

    public ClassHandler() {
        this(ClassHandler.class.getClassLoader());
    }

    public ClassHandler(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public static ClassHandler fromClassLoader() {
        return fromClassLoader(ClassHandler.class.getClassLoader());
    }

    public static ClassHandler fromClassLoader(ClassLoader classLoader) {
        return new ClassHandler() {
            @Override
            public Class<?> loadClass(String className) throws ClassNotFoundException {
                return classLoader.loadClass(className);
            }
        };
    }

    public abstract Class<?> loadClass(String className) throws ClassNotFoundException;

    public static ClassHandler fromPackage(final String packageName) {
        return new ClassHandler() {
            @Override
            public Class<?> loadClass(String className) throws ClassNotFoundException {
                return this.classLoader.loadClass(packageName + "." + className);
            }
        };
    }
}
