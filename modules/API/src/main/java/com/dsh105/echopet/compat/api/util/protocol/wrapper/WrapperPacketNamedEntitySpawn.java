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

import com.dsh105.echopet.compat.api.util.protocol.Packet;
import com.dsh105.echopet.compat.api.util.protocol.PacketFactory;

public class WrapperPacketNamedEntitySpawn extends Packet {

    public WrapperPacketNamedEntitySpawn() {
        super(PacketFactory.PacketType.NAMED_ENTITY_SPAWN);
    }

    public void setEntityId(int id) {
        this.write("a", id);
    }

    public int getEntityId() {
        return (Integer) this.read("a");
    }

    public void setGameProfile(Object profile) {
        this.write("b", profile);
    }

    public Object getGameProfile() {
        return this.read("b");
    }

    public void setX(double value) {
        this.write("c", (int) Math.floor(value * 32.0D));
    }

    public double getX() {
        return (((Integer) this.read("c")) / 32.0D);
    }

    public void setY(double value) {
        this.write("d", (int) Math.floor(value * 32.0D));
    }

    public double getY() {
        return (((Integer) this.read("d")) / 32.0D);
    }

    public void setZ(double value) {
        this.write("e", (int) Math.floor(value * 32.0D));
    }

    public double getZ() {
        return (((Integer) this.read("e")) / 32.0D);
    }

    public void setYaw(float value) {
        this.write("f", (byte) (value * 256.0F / 360.0F));
    }

    public float getYaw() {
        return (((Byte) this.read("f")) * 360.0F / 256.0F);
    }

    public void setPitch(float value) {
        this.write("g", (byte) (value * 256.0F / 360.0F));
    }

    public float getPitch() {
        return (((Byte) this.read("g")) * 360.0F / 256.0F);
    }

    public void setEquipmentId(int id) {
        this.write("h", id);
    }

    public int getEquipmentId() {
        return (Integer) this.read("h");
    }

    public void setMetadata(WrappedDataWatcher metadata) {
        this.write("i", metadata.getHandle());
    }

    public Object getMetadata() {
        return this.read("i");
    }
}