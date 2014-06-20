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

import com.google.common.base.Preconditions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassPackageMapper {

    protected String packageName;
    protected ClassHandler classHandler;
    protected Map<String, Class<?>> classes = new ConcurrentHashMap<String, Class<?>>();

    public ClassPackageMapper(String packageName, ClassHandler classHandler) {
        this.packageName = packageName;
        this.classHandler = classHandler;
    }

    public Class<?> getClass(String className) {
        try {
            Class<?> clazz = this.classes.get(Preconditions.checkNotNull(className, "ClassName can't be NULL!"));

            if (clazz == null) {


                clazz = this.classHandler.loadClass(this.packageName + "." + className);

                if (clazz == null)
                    throw new ClassNotFoundException("Failed to find class: " + this.packageName + "." + className);

                this.classes.put(className, clazz);
            }

            return clazz;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to find class: " + this.packageName + "." + className);
        }
    }
}
