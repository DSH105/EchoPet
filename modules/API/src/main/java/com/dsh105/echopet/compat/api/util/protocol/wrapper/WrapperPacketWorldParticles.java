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

public class WrapperPacketWorldParticles extends Packet {

    private static String field_a = ReflectionUtil.isServerMCPC() ? "field_98209_a" : "a";
    private static String field_b = ReflectionUtil.isServerMCPC() ? "field_98207_b" : "b";
    private static String field_c = ReflectionUtil.isServerMCPC() ? "field_98208_c" : "c";
    private static String field_d = ReflectionUtil.isServerMCPC() ? "field_98205_d" : "d";
    private static String field_e = ReflectionUtil.isServerMCPC() ? "field_98206_e" : "e";
    private static String field_f = ReflectionUtil.isServerMCPC() ? "field_98203_f" : "f";
    private static String field_g = ReflectionUtil.isServerMCPC() ? "field_98204_g" : "g";
    private static String field_h = ReflectionUtil.isServerMCPC() ? "field_98210_h" : "h";
    private static String field_i = ReflectionUtil.isServerMCPC() ? "field_98211_i" : "i";

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
        this.write(field_a, value);
    }

    public String getParticle() {
        return (String) this.read(field_a);
    }

    public void setX(float value) {
        this.write(field_b, value);
    }

    public float getX() {
        return (Float) this.read(field_b);
    }

    public void setY(float value) {
        this.write(field_c, value);
    }

    public float getY() {
        return (Float) this.read(field_c);
    }

    public void setZ(float value) {
        this.write(field_d, value);
    }

    public float getZ() {
        return (Float) this.read(field_d);
    }

    public void setOffsetX(float value) {
        this.write(field_e, value);
    }

    public float getOffsetX() {
        return (Float) this.read(field_e);
    }

    public void setOffsetY(float value) {
        this.write(field_f, value);
    }

    public float getOffsetY() {
        return (Float) this.read(field_f);
    }

    public void setOffsetZ(float value) {
        this.write(field_g, value);
    }

    public float getOffsetZ() {
        return (Float) this.read(field_g);
    }

    public void setParticleSpeed(float speed) {
        this.write(field_h, speed);
    }

    public float getParticleSpeed() {
        return (Float) this.read(field_h);
    }

    public void setParticleAmount(int amount) {
        this.write(field_i, amount);
    }

    public int getParticleAmount() {
        return (Integer) this.read(field_i);
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