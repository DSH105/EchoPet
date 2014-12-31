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

package com.dsh105.echopet.bridge;

import com.dsh105.commodus.ServerUtil;
import org.bukkit.ChatColor;
import org.spongepowered.api.text.format.TextColors;

import java.lang.reflect.Field;

public final class ColorBridge {

    private static Object BLACK;
    private static Object DARK_BLUE;
    private static Object DARK_GREEN;
    private static Object DARK_AQUA;
    private static Object DARK_RED;
    private static Object DARK_PURPLE;
    private static Object GOLD;
    private static Object GRAY;
    private static Object DARK_GRAY;
    private static Object BLUE;
    private static Object GREEN;
    private static Object AQUA;
    private static Object RED;
    private static Object LIGHT_PURPLE;
    private static Object YELLOW;
    private static Object WHITE;
    
    static {
        for (Field field : ColorBridge.class.getFields()) {
            Object value = null;
            switch (ServerUtil.getServerBrand().getCapsule()) {
                case BUKKIT:
                    value = ChatColor.valueOf(field.getName());
                    break;
                case SPONGE:
                    value = TextColors.valueOf(field.getName());
                    break;
            }
            try {
                field.set(null, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to provide text color bridge: " + field.getName(), e);
            }
        }
        
    }
}