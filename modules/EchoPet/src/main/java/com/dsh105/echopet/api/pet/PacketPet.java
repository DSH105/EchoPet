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

package com.dsh105.echopet.api.pet;

import com.dsh105.echopet.compat.api.entity.IEntityPacketPet;
import com.dsh105.echopet.compat.api.entity.IEntityPet;
import com.dsh105.echopet.compat.api.entity.IPacketPet;
import org.bukkit.entity.Player;

public class PacketPet extends Pet implements IPacketPet {

    public PacketPet(Player owner) {
        super(owner);
    }

    public PacketPet(String owner, IEntityPet entityPet) {
        super(owner, entityPet);
    }

    @Override
    public IEntityPacketPet getEntityPet() {
        return (IEntityPacketPet) super.getEntityPet();
    }
}