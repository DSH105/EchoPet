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
import com.dsh105.echopet.compat.api.util.protocol.Packet;
import com.dsh105.echopet.compat.api.util.protocol.PacketFactory;

public class WrapperPacketWorldParticles extends Packet {

    public WrapperPacketWorldParticles() {
        super(PacketFactory.PacketType.WORLD_PARTICLES);
    }

    public void setParticleType(ParticleType value) {
        this.write("a", value.getName());
        this.setParticleSpeed(value.getDefaultSpeed());
        this.setParticleAmount(value.getDefaultAmount());
    }

    public ParticleType getParticleType() {
        return ParticleType.fromName(this.getParticle());
    }

    public void setParticle(String value) {
        this.write(ReflectionConstants.PACKET_PARTICLES_FIELD_NAME.getName(), value);
    }

    public String getParticle() {
        return (String) this.read(ReflectionConstants.PACKET_PARTICLES_FIELD_NAME.getName());
    }

    public void setX(float value) {
        this.write(ReflectionConstants.PACKET_PARTICLES_FIELD_X.getName(), value);
    }

    public float getX() {
        return (Float) this.read(ReflectionConstants.PACKET_PARTICLES_FIELD_X.getName());
    }

    public void setY(float value) {
        this.write(ReflectionConstants.PACKET_PARTICLES_FIELD_Y.getName(), value);
    }

    public float getY() {
        return (Float) this.read(ReflectionConstants.PACKET_PARTICLES_FIELD_Y.getName());
    }

    public void setZ(float value) {
        this.write(ReflectionConstants.PACKET_PARTICLES_FIELD_Z.getName(), value);
    }

    public float getZ() {
        return (Float) this.read(ReflectionConstants.PACKET_PARTICLES_FIELD_Z.getName());
    }

    public void setOffsetX(float value) {
        this.write(ReflectionConstants.PACKET_PARTICLES_FIELD_OFFSETX.getName(), value);
    }

    public float getOffsetX() {
        return (Float) this.read(ReflectionConstants.PACKET_PARTICLES_FIELD_OFFSETX.getName());
    }

    public void setOffsetY(float value) {
        this.write(ReflectionConstants.PACKET_PARTICLES_FIELD_OFFSETY.getName(), value);
    }

    public float getOffsetY() {
        return (Float) this.read(ReflectionConstants.PACKET_PARTICLES_FIELD_OFFSETY.getName());
    }

    public void setOffsetZ(float value) {
        this.write(ReflectionConstants.PACKET_PARTICLES_FIELD_OFFSETZ.getName(), value);
    }

    public float getOffsetZ() {
        return (Float) this.read(ReflectionConstants.PACKET_PARTICLES_FIELD_OFFSETZ.getName());
    }

    public void setParticleSpeed(float speed) {
        this.write(ReflectionConstants.PACKET_PARTICLES_FIELD_SPEED.getName(), speed);
    }

    public float getParticleSpeed() {
        return (Float) this.read(ReflectionConstants.PACKET_PARTICLES_FIELD_SPEED.getName());
    }

    public void setParticleAmount(int amount) {
        this.write(ReflectionConstants.PACKET_PARTICLES_FIELD_QUANTITY.getName(), amount);
    }

    public int getParticleAmount() {
        return (Integer) this.read(ReflectionConstants.PACKET_PARTICLES_FIELD_QUANTITY.getName());
    }

    public enum ParticleType {
        SMOKE("largesmoke", 0.2f, 20),
        RED_SMOKE("reddust", 0f, 40),
        RAINBOW_SMOKE("reddust", 1f, 100),
        FIRE("flame", 0.05f, 100),
        HEART("heart", 0f, 4),
        MAGIC_RUNES("enchantmenttable", 1f, 100),
        LAVA_SPARK("lava", 0f, 4),
        SPLASH("splash", 1f, 40),
        PORTAL("portal", 1f, 100),

        EXPLOSION("largeexplode", 0.1f, 1),
        HUGE_EXPLOSION("hugeexplosion", 0.1f, 1),
        CLOUD("explode", 0.1f, 10),
        CRITICAL("crit", 0.1f, 100),
        MAGIC_CRITIAL("magicCrit", 0.1f, 100),
        ANGRY_VILLAGER("angryVillager", 0f, 20),
        SPARKLE("happyVillager", 0f, 20),
        WATER_DRIP("dripWater", 0f, 100),
        LAVA_DRIP("dripLava", 0f, 100),
        WITCH_MAGIC("witchMagic", 1f, 20),

        SNOWBALL("snowballpoof", 1f, 20),
        SNOW_SHOVEL("snowshovel", 0.02f, 30),
        SLIME_SPLAT("slime", 1f, 30),
        BUBBLE("bubble", 0f, 50),
        SPELL_AMBIENT("mobSpellAmbient", 1f, 100),
        VOID("townaura", 1f, 100),

        BLOCK_BREAK("blockcrack", 0.1F, 100),
        BLOCK_DUST("blockdust", 0.1F, 100),;

        private String name;
        private float defaultSpeed;
        private int defaultAmount;

        ParticleType(String name, float defaultSpeed, int defaultAmount) {
            this.name = name;
            this.defaultSpeed = defaultSpeed;
            this.defaultAmount = defaultAmount;
        }

        public String getName() {
            return name;
        }

        public float getDefaultSpeed() {
            return defaultSpeed;
        }

        public int getDefaultAmount() {
            return defaultAmount;
        }

        public static ParticleType fromName(String name) {
            for (ParticleType type : ParticleType.values()) {
                if (type.getName().equalsIgnoreCase(name)) {
                    return type;
                }
            }
            return null;
        }
    }
}