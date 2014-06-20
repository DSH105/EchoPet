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

package com.dsh105.echopet.util.protocol.wrapper;

import com.captainbern.reflection.ClassTemplate;
import com.captainbern.reflection.Reflection;

import java.util.UUID;

public class WrappedGameProfile extends com.captainbern.minecraft.wrapper.AbstractWrapper {

    private static ClassTemplate GAME_PROFILE_TEMPLATE = new Reflection().reflect("net.minecraft.util.com.mojang.authlib.GameProfile");

    private WrappedGameProfile(Object ident, String name) {
        super(GAME_PROFILE_TEMPLATE.getReflectedClass());
        if (ident instanceof UUID || ident instanceof String) {
            super.setHandle(GAME_PROFILE_TEMPLATE.getSafeConstructor(ident.getClass(), String.class).getAccessor().invoke(ident, name));
        } else {
            throw new IllegalArgumentException("Invalid ident entered!");
        }
    }

    public WrappedGameProfile(UUID uuid, String name) {
        this((Object) uuid, name);
    }

    public WrappedGameProfile(String ident, String name) {
        this((Object) ident, name);
    }

    public static WrappedGameProfile getNewProfile(WrappedGameProfile old, String newName) {
        return new WrappedGameProfile(old.getId(), newName);
    }

    public UUID getUniqueId() {
        return getId();
    }

    public String getIdent() {
        return getId();
    }

    private <T> T getId() {
        return (T) GAME_PROFILE_TEMPLATE.getSafeMethod("getId").getAccessor().invoke(getHandle());
    }
}
