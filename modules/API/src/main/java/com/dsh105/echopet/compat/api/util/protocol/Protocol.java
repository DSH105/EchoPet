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

package com.dsh105.echopet.compat.api.util.protocol;

import com.dsh105.echopet.compat.api.util.ReflectionUtil;

public enum Protocol {

    HANDSHAKE,
    PLAY,
    STATUS,
    LOGIN;

    public static Protocol fromVanilla(Enum<?> enumValue) {
        String name = enumValue.name();

        if ("HANDSHAKING".equals(name)) {
            return HANDSHAKE;
        }
        if ("PLAY".equals(name)) {
            return PLAY;
        }
        if ("STATUS".equals(name)) {
            return STATUS;
        }
        if ("LOGIN".equals(name)) {
            return LOGIN;
        }

        return null;
    }

    public Object toVanilla() {
        switch (this) {
            case HANDSHAKE:
                return Enum.valueOf(ReflectionUtil.getNMSClass("EnumProtocol"), "HANDSHAKING");
            case PLAY:
                return Enum.valueOf(ReflectionUtil.getNMSClass("EnumProtocol"), "PLAY");
            case STATUS:
                return Enum.valueOf(ReflectionUtil.getNMSClass("EnumProtocol"), "STATUS");
            case LOGIN:
                return Enum.valueOf(ReflectionUtil.getNMSClass("EnumProtocol"), "LOGIN");
            default:
                return null;
        }
    }
}
