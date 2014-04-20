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

/**
 * Used to provide functionality with MCPC+, as MCPC+ remaps the classes in order to
 * let Mods and Plugins work together in harmony. This is however a downside for Plugins
 * since the Plugins will be remapped and the mods remain untouched.
 *
 * For more info/details about this:
 *
 *     The PluginClassLoader:
 *     https://github.com/MinecraftPortCentral/MCPC-Plus/blob/master/patches/org/bukkit/plugin/java/PluginClassLoader.java.patch
 *
 *     The Remapper is one provided by md_5, it's part of his "SpecialSource" project.
 *     It uses some basic ASM to remap the classed of a jar. Fortunately for us, this remapper is easy reachable
 *     and easy to use since we can get the remapped-classnames fairly easy
 *     ( https://github.com/llbit/ow2-asm/blob/master/src/org/objectweb/asm/commons/Remapper.java#L220 )
 */
package com.dsh105.echopet.compat.api.reflection.utility;

import com.dsh105.echopet.compat.api.reflection.ClassTemplate;
import com.dsh105.echopet.compat.api.reflection.MethodAccessor;
import org.bukkit.Bukkit;

public class RemappedClassHandler extends ClassHandler {

    protected ClassLoader classLoader;
    protected Object remapper;
    protected MethodAccessor<String> map;

    public RemappedClassHandler() {
        this(RemappedClassHandler.class.getClassLoader());
    }

    public RemappedClassHandler(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    protected RemappedClassHandler initialize() throws UnsupportedOperationException, IllegalStateException {
        if (Bukkit.getServer() == null || !Bukkit.getServer().getVersion().contains("MCPC-Plus")) {
            throw new UnsupportedOperationException("Remapper not available!");
        }

        this.remapper = ClassTemplate.create(this.classLoader.getClass()).getField("remapper").get(getClass().getClassLoader());

        if (this.remapper == null)
            throw new IllegalStateException("Remapper is NULL!");

        Class<?> remapperClass = this.remapper.getClass();
        this.map = ClassTemplate.create(remapperClass).getMethod("map", String.class);
        return this;
    }

    public String getRemappedName(String className) {
        return map.invoke(remapper, className.replace('.', '/')).replace('/', '.');
    }

    @Override
    public Class<?> loadClass(String className) throws ClassNotFoundException {
        try {
            return this.classLoader.loadClass(getRemappedName(className));
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Failed to find class: " + className + " (Remapped class-name: " + getRemappedName(className) + ")");
        }
    }
}
