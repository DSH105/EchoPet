/*
 * This file is part of EchoPet-API.
 *
 * EchoPet-API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EchoPet-API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet-API.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.compat.api.util;

import com.dsh105.dshutils.util.GeometryUtil;
import com.dsh105.echopet.compat.api.util.protocol.wrapper.WrapperPacketWorldParticles;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Random;

public class ParticleUtil {

    private static Random RANDOM = new Random();

    private static WrapperPacketWorldParticles createPacket(WrapperPacketWorldParticles.ParticleType type, Location location) {
        return createPacket(type, location, r());
    }

    private static WrapperPacketWorldParticles createPacket(WrapperPacketWorldParticles.ParticleType type, Location location, Vector offset) {
        return createPacket(type, location, offset, type.getDefaultSpeed(), type.getDefaultAmount());
    }

    private static WrapperPacketWorldParticles createPacket(WrapperPacketWorldParticles.ParticleType type, Location location, Vector offset, float speed, int amount) {
        return createPacket(type.getName(), location, offset, speed, amount);
    }

    private static WrapperPacketWorldParticles createPacket(String name, Location location) {
        WrapperPacketWorldParticles.ParticleType type = WrapperPacketWorldParticles.ParticleType.fromName(name);
        return createPacket(name, location, r(), type.getDefaultSpeed(), type.getDefaultAmount());
    }

    private static WrapperPacketWorldParticles createPacket(String name, Location location, Vector offset, float speed, int amount) {
        WrapperPacketWorldParticles particles = new WrapperPacketWorldParticles();
        particles.setParticle(name);
        particles.setX((float) location.getX());
        particles.setY((float) location.getY());
        particles.setZ((float) location.getZ());
        particles.setOffsetX((float) offset.getX());
        particles.setOffsetY((float) offset.getY());
        particles.setOffsetZ((float) offset.getZ());
        particles.setParticleSpeed(speed);
        particles.setParticleAmount(amount);
        return particles;
    }

    private static Vector r() {
        return new Vector(RANDOM.nextFloat(), RANDOM.nextFloat(), RANDOM.nextFloat());
    }

    public static void show(WrapperPacketWorldParticles.ParticleType type, Location l) {
        for (Player p : GeometryUtil.getNearbyPlayers(l, 50)) {
            createPacket(type, l).send(p);
        }
    }

    public static void show(WrapperPacketWorldParticles.ParticleType type, Location l, Vector v, float speed, int amount) {
        for (Player p : GeometryUtil.getNearbyPlayers(l, 50)) {
            createPacket(type, l, v, speed, amount).send(p);
        }
    }

    public static void showPlayer(WrapperPacketWorldParticles.ParticleType type, Player p, Location l) {
        createPacket(type, l).send(p);
    }

    public static void showPlayer(WrapperPacketWorldParticles.ParticleType type, Player p, Location l, Vector v, float speed, int amount) {
        createPacket(type, l, v, speed, amount).send(p);
    }

    public static void showWithData(WrapperPacketWorldParticles.ParticleType type, Location l, int blockId, int blockMeta) {
        for (Player p : GeometryUtil.getNearbyPlayers(l, 50)) {
            createPacket(type.getName() + "_" + blockId + "_" + blockMeta, l, r(), type.getDefaultSpeed(), type.getDefaultAmount()).send(p);
        }
    }

    public static void showWithData(WrapperPacketWorldParticles.ParticleType type, Location l, Vector v, float speed, int amount, int blockId, int blockMeta) {
        for (Player p : GeometryUtil.getNearbyPlayers(l, 50)) {
            createPacket(type.getName() + "_" + blockId + blockMeta, l, v, speed, amount).send(p);
        }
    }

    public static void showWithDataToPlayer(WrapperPacketWorldParticles.ParticleType type, Location l, Player p, int blockId, int blockMeta) {
        createPacket(type.getName() + "_" + blockId + blockMeta, l, r(), type.getDefaultSpeed(), type.getDefaultAmount()).send(p);
    }

    public static void showWithDataToPlayer(WrapperPacketWorldParticles.ParticleType type, Location l, Player p, Vector v, float speed, int amount, int blockId, int blockMeta) {
        createPacket(type.getName() + "_" + blockId + blockMeta, l, v, speed, amount).send(p);
    }
}