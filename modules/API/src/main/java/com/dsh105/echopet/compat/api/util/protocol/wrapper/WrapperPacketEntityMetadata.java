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

package com.dsh105.echopet.compat.api.util.protocol.wrapper;

import com.dsh105.echopet.compat.api.util.ReflectionUtil;
import com.dsh105.echopet.compat.api.util.protocol.Packet;
import com.dsh105.echopet.compat.api.util.protocol.PacketFactory;
import com.dsh105.echopet.compat.api.reflection.SafeMethod;

public class WrapperPacketEntityMetadata extends Packet {

    private static String field_a = ReflectionUtil.isServerMCPC() ? "field_73393_a" : "a";
    private static String field_b = ReflectionUtil.isServerMCPC() ? "field_73392_b" : "b";
    private static String func_c = ReflectionUtil.isServerMCPC() ? "func_75685_c" : "c";

    public WrapperPacketEntityMetadata() {
        super(PacketFactory.PacketType.ENTITY_METADATA);
    }

    public void setEntityId(int value) {
        this.write(field_a, value);
    }

    public int getEntityId() {
        return (Integer) this.read(field_a);
    }

    public void setMetadata(WrappedDataWatcher metadata) {
        Object handle = metadata.getHandle();
        this.write(field_a, new SafeMethod<Void>(handle.getClass(), func_c).invoke(handle));
    }

    public Object getMetadata() {
        return this.read(field_b);
    }
}