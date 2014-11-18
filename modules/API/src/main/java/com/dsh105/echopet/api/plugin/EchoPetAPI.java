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

package com.dsh105.echopet.api.plugin;

import com.captainbern.minecraft.conversion.BukkitUnwrapper;
import com.dsh105.echopet.api.entity.entitypet.EntityPet;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

// TODO: implement general API methods
public class EchoPetAPI {

    public static boolean isPetEntity(Entity bukkitEntity) {
        if (bukkitEntity instanceof LivingEntity) {
            if (BukkitUnwrapper.getInstance().unwrap(bukkitEntity) instanceof EntityPet) {
                return true;
            }
        }
        return false;
    }
}