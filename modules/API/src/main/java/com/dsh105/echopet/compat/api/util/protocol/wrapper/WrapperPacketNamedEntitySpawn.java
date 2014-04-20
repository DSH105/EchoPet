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

public class WrapperPacketNamedEntitySpawn extends Packet {

    private static String field_a = ReflectionUtil.isServerMCPC() ? "field_73516_a" : "a";
    private static String field_b = ReflectionUtil.isServerMCPC() ? "field_73514_b" : "b";
    private static String field_c = ReflectionUtil.isServerMCPC() ? "field_73515_c" : "c";
    private static String field_d = ReflectionUtil.isServerMCPC() ? "field_73512_d" : "d";
    private static String field_e = ReflectionUtil.isServerMCPC() ? "field_73513_e" : "e";
    private static String field_f = ReflectionUtil.isServerMCPC() ? "field_73510_f" : "f";
    private static String field_g = ReflectionUtil.isServerMCPC() ? "field_73511_g" : "g";
    private static String field_h = ReflectionUtil.isServerMCPC() ? "field_73518_h" : "h";
    private static String field_i = ReflectionUtil.isServerMCPC() ? "field_73519_i" : "i";

    public WrapperPacketNamedEntitySpawn() {
        super(PacketFactory.PacketType.NAMED_ENTITY_SPAWN);
    }

    public void setEntityId(int id) {
        this.write(field_a, id);
    }

    public int getEntityId() {
        return (Integer) this.read(field_a);
    }

    public void setGameProfile(Object profile) {
        this.write(field_b, profile);
    }

    public Object getGameProfile() {
        return this.read(field_b);
    }

    public void setX(double value) {
        this.write(field_c, (int) Math.floor(value * 32.0D));
    }

    public double getX() {
        return (((Integer) this.read(field_c)) / 32.0D);
    }

    public void setY(double value) {
        this.write(field_d, (int) Math.floor(value * 32.0D));
    }

    public double getY() {
        return (((Integer) this.read(field_d)) / 32.0D);
    }

    public void setZ(double value) {
        this.write(field_e, (int) Math.floor(value * 32.0D));
    }

    public double getZ() {
        return (((Integer) this.read(field_e)) / 32.0D);
    }

    public void setYaw(float value) {
        this.write(field_f, (byte) (value * 256.0F / 360.0F));
    }

    public float getYaw() {
        return (((Byte) this.read(field_f)) * 360.0F / 256.0F);
    }

    public void setPitch(float value) {
        this.write(field_g, (byte) (value * 256.0F / 360.0F));
    }

    public float getPitch() {
        return (((Byte) this.read(field_g)) * 360.0F / 256.0F);
    }

    public void setEquipmentId(int id) {
        this.write(field_h, id);
    }

    public int getEquipmentId() {
        return (Integer) this.read(field_h);
    }

    public void setMetadata(WrappedDataWatcher metadata) {
        this.write(field_i, metadata.getHandle());
    }

    public Object getMetadata() {
        return this.read(field_i);
    }
}