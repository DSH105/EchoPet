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

package com.dsh105.echopet.compat.api.reflection.utility;

import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.reflection.FieldAccessor;
import com.dsh105.echopet.compat.api.reflection.SafeField;
import org.bukkit.entity.Player;

public class CommonFields {

    private static volatile FieldAccessor<Object> CONNECTION_ACCESSOR;
    private static volatile FieldAccessor<Object> NETWORK_ACCESSOR;

    private CommonFields() {
        super();
    }

    public static Object getNetworkManager(Player player) {
        Object vanillaPlayer = CommonMethods.getVanillaObject(player);

        if(NETWORK_ACCESSOR == null) {
           NETWORK_ACCESSOR = new SafeField<Object>(CommonReflection.getPlayerConnectionClass(), "networkManager");

            if(!NETWORK_ACCESSOR.getField().getType().equals(CommonReflection.getNetworkManagerClass())) {
                EchoPet.getPlugin().getReflectionLogger().warning("Failed to retrieve a valid NetworkManager!");
                NETWORK_ACCESSOR = null;
                return null;
            }
        }
        return NETWORK_ACCESSOR.get(vanillaPlayer);
    }

    public static Object getPlayerConnection(Player player) {
        Object vanillaPlayer = CommonMethods.getVanillaObject(player);

        if(CONNECTION_ACCESSOR == null) {
            CONNECTION_ACCESSOR = new SafeField<Object>(CommonReflection.getEntityPlayerClass(), "playerConnection");

            if(!CONNECTION_ACCESSOR.getField().getType().equals(CommonReflection.getPlayerConnectionClass())) {
                EchoPet.getPlugin().getReflectionLogger().warning("Failed to retrieve a valid PlayerConnection!");
                CONNECTION_ACCESSOR = null;
                return null;
            }
        }

        return CONNECTION_ACCESSOR.get(vanillaPlayer);
    }
}
