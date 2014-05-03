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

import com.dsh105.echopet.compat.api.reflection.ReflectionConstants;
import com.dsh105.echopet.compat.api.util.ReflectionUtil;
import com.dsh105.echopet.compat.api.util.protocol.Packet;
import com.dsh105.echopet.compat.api.util.protocol.PacketFactory;

public class WrapperPacketNamedEntitySpawn extends Packet {

    public WrapperPacketNamedEntitySpawn() {
        super(PacketFactory.PacketType.NAMED_ENTITY_SPAWN);
    }

    public void setEntityId(int id) {
        this.write(ReflectionConstants.PACKET_NAMEDSPAWN_FIELD_ID.getName(), id);
    }

    public int getEntityId() {
        return (Integer) this.read(ReflectionConstants.PACKET_NAMEDSPAWN_FIELD_ID.getName());
    }

    @Deprecated
    public void setGameProfile(String profileName) {
        this.write(ReflectionConstants.PACKET_NAMEDSPAWN_FIELD_PROFILE.getName(), profileName);
    }

    public void setGameProfile(WrappedGameProfile profile) {
        this.write(ReflectionConstants.PACKET_NAMEDSPAWN_FIELD_PROFILE.getName(), profile.getHandle());
    }

    public Object getGameProfile() {
        return this.read(ReflectionConstants.PACKET_NAMEDSPAWN_FIELD_PROFILE.getName());
    }

    public void setX(double value) {
        this.write(ReflectionConstants.PACKET_NAMEDSPAWN_FIELD_X.getName(), (int) Math.floor(value * 32.0D));
    }

    public double getX() {
        return (((Integer) this.read(ReflectionConstants.PACKET_NAMEDSPAWN_FIELD_X.getName())) / 32.0D);
    }

    public void setY(double value) {
        this.write(ReflectionConstants.PACKET_NAMEDSPAWN_FIELD_Y.getName(), (int) Math.floor(value * 32.0D));
    }

    public double getY() {
        return (((Integer) this.read(ReflectionConstants.PACKET_NAMEDSPAWN_FIELD_Y.getName())) / 32.0D);
    }

    public void setZ(double value) {
        this.write(ReflectionConstants.PACKET_NAMEDSPAWN_FIELD_Z.getName(), (int) Math.floor(value * 32.0D));
    }

    public double getZ() {
        return (((Integer) this.read(ReflectionConstants.PACKET_NAMEDSPAWN_FIELD_Z.getName())) / 32.0D);
    }

    public void setYaw(float value) {
        this.write(ReflectionConstants.PACKET_NAMEDSPAWN_FIELD_YAW.getName(), (byte) (value * 256.0F / 360.0F));
    }

    public float getYaw() {
        return (((Byte) this.read(ReflectionConstants.PACKET_NAMEDSPAWN_FIELD_YAW.getName())) * 360.0F / 256.0F);
    }

    public void setPitch(float value) {
        this.write(ReflectionConstants.PACKET_NAMEDSPAWN_FIELD_PITCH.getName(), (byte) (value * 256.0F / 360.0F));
    }

    public float getPitch() {
        return (((Byte) this.read(ReflectionConstants.PACKET_NAMEDSPAWN_FIELD_PITCH.getName())) * 360.0F / 256.0F);
    }

    public void setEquipmentId(int id) {
        this.write(ReflectionConstants.PACKET_NAMEDSPAWN_FIELD_EQUIPMENT.getName(), id);
    }

    public int getEquipmentId() {
        return (Integer) this.read(ReflectionConstants.PACKET_NAMEDSPAWN_FIELD_EQUIPMENT.getName());
    }

    public void setMetadata(WrappedDataWatcher metadata) {
        this.write(ReflectionConstants.PACKET_NAMEDSPAWN_FIELD_META.getName(), metadata.getHandle());
    }

    public Object getMetadata() {
        return this.read(ReflectionConstants.PACKET_NAMEDSPAWN_FIELD_META.getName());
    }
}