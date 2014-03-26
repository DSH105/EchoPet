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

package io.github.dsh105.echopet.util;

import com.dsh105.dshutils.logger.ConsoleLogger;
import com.dsh105.dshutils.logger.Logger;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PlayerUtil {

    private static final Method sendPacket = ReflectionUtil.getMethod(ReflectionUtil.getNMSClass("PlayerConnection"), "sendPacket", ReflectionUtil.getNMSClass("Packet"));

    public static void sendPacket(Player player, Object packet) {
        Object playerConnection = getPlayerConnection(player);
        try {
            sendPacket.invoke(playerConnection, packet);
        } catch (IllegalAccessException e) {
            ConsoleLogger.log(Logger.LogLevel.WARNING, "Failed to retrieve the PlayerConnection of: " + player.getName());
        } catch (IllegalArgumentException e) {
            ConsoleLogger.log(Logger.LogLevel.WARNING, "Failed to retrieve the PlayerConnection of: " + player.getName());
        } catch (InvocationTargetException e) {
            ConsoleLogger.log(Logger.LogLevel.WARNING, "Failed to retrieve the PlayerConnection of: " + player.getName());
        }
    }

    public static Object playerToEntityPlayer(Player player) {
        Method getHandle = ReflectionUtil.getMethod(player.getClass(), "getHandle");
        try {
            return getHandle.invoke(player);
        } catch (IllegalAccessException e) {
            ConsoleLogger.log(Logger.LogLevel.WARNING, "Failed retrieve the NMS Player-Object of:" + player.getName());
            return null;
        } catch (IllegalArgumentException e) {
            ConsoleLogger.log(Logger.LogLevel.WARNING, "Failed retrieve the NMS Player-Object of:" + player.getName());
            return null;
        } catch (InvocationTargetException e) {
            ConsoleLogger.log(Logger.LogLevel.WARNING, "Failed retrieve the NMS Player-Object of:" + player.getName());
            return null;
        }
    }

    public static Object getPlayerConnection(Player player) {
        return ReflectionUtil.getField(ReflectionUtil.getNMSClass("EntityPlayer"), "playerConnection", playerToEntityPlayer(player));
    }
}
