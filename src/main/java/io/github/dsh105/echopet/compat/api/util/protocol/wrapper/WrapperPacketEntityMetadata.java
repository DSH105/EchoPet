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

package io.github.dsh105.echopet.compat.api.util.protocol.wrapper;

import io.github.dsh105.echopet.compat.api.util.protocol.Packet;
import io.github.dsh105.echopet.compat.api.util.protocol.PacketFactory;
import io.github.dsh105.echopet.compat.api.util.reflection.SafeMethod;

public class WrapperPacketEntityMetadata extends Packet {

    public WrapperPacketEntityMetadata() {
        super(PacketFactory.PacketType.ENTITY_METADATA);
    }

    public void setEntityId(int value) {
        this.write("a", value);
    }

    public int getEntityId() {
        return (Integer) this.read("a");
    }

    public void setMetadata(WrappedDataWatcher metadata) {
        Object handle = metadata.getHandle();
        this.write("b", new SafeMethod<Void>(handle.getClass(), "c").invoke(handle));
    }
}