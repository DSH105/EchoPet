package io.github.dsh105.echopet.util;

import net.minecraft.server.v1_7_R1.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Random;

public enum Particle {

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
    ICON_CRACK("blockdust", 0.1F, 100),
    BLOCK_DUST("blockdust", 0.1F, 100),
    ;

	/*
     * Unused effects
	 * FIREWORK_SPARK("fireworksSpark", 0f, 50, "firework_spark"),
	 * MOB_SPELL("mobSpell", 2f, 50, "" "mob_spell"),
	 * SPELL("spell", 1f, 50, "" "spell"),
	 * INSTANT_SPELL("instantSpell", 1f, 50, "instant_spell")
	 */

    private String particleName;
    private float defaultSpeed;
    private int particleAmount;

    Particle(String particleName, float defaultSpeed, int particleAmount) {
        this.particleName = particleName;
        this.defaultSpeed = defaultSpeed;
        this.particleAmount = particleAmount;
    }

    public String getParticleName() {
        return this.particleName;
    }

    public int getParticleAmount() {
        return this.particleAmount;
    }

    public float getDefaultSpeed() {
        return this.defaultSpeed;
    }

    public void sendTo(World world, double x, double y, double z) throws Exception {
        Object packet = Class.forName(
                "net.minecraft.server." + ReflectionUtil.getVersionString()
                        + ".PacketPlayOutWorldParticles").getConstructors()[0].newInstance();
        ReflectionUtil.setValue(packet, "a", particleName);
        ReflectionUtil.setValue(packet, "b", (float) x);
        ReflectionUtil.setValue(packet, "c", (float) y);
        ReflectionUtil.setValue(packet, "d", (float) z);
        ReflectionUtil.setValue(packet, "e", new Random().nextFloat());
        ReflectionUtil.setValue(packet, "f", new Random().nextFloat());
        ReflectionUtil.setValue(packet, "g", new Random().nextFloat());
        ReflectionUtil.setValue(packet, "h", defaultSpeed);
        ReflectionUtil.setValue(packet, "i", particleAmount);
        ReflectionUtil.sendPacket(new Location(world, x, y, z), packet);
    }

    public void sendTo(World world, double x, double y, double z, Vector v) throws Exception {
        Object packet = Class.forName(
                "net.minecraft.server." + ReflectionUtil.getVersionString()
                        + ".PacketPlayOutWorldParticles").getConstructors()[0].newInstance();
        ReflectionUtil.setValue(packet, "a", particleName);
        ReflectionUtil.setValue(packet, "b", (float) x);
        ReflectionUtil.setValue(packet, "c", (float) y);
        ReflectionUtil.setValue(packet, "d", (float) z);
        ReflectionUtil.setValue(packet, "e", v.getX());
        ReflectionUtil.setValue(packet, "f", v.getY());
        ReflectionUtil.setValue(packet, "g", v.getZ());
        ReflectionUtil.setValue(packet, "h", defaultSpeed);
        ReflectionUtil.setValue(packet, "i", particleAmount);
        ReflectionUtil.sendPacket(new Location(world, x, y, z), packet);
    }

    public void sendTo(Location l) throws Exception {
        this.sendTo(l.getWorld(), l.getX(), l.getY(), l.getZ());
    }

    public void sendTo(Location l, Vector v) throws Exception {
        this.sendTo(l.getWorld(), l.getX(), l.getY(), l.getZ(), v);
    }

    public void sendToPlayer(Location l, Player p) throws Exception {
        Object packet = Class.forName(
                "net.minecraft.server." + ReflectionUtil.getVersionString()
                        + ".PacketPlayOutWorldParticles").getConstructors()[0].newInstance();
        ReflectionUtil.setValue(packet, "a", particleName);
        ReflectionUtil.setValue(packet, "b", (float) l.getX());
        ReflectionUtil.setValue(packet, "c", (float) l.getY());
        ReflectionUtil.setValue(packet, "d", (float) l.getZ());
        ReflectionUtil.setValue(packet, "e", new Random().nextFloat());
        ReflectionUtil.setValue(packet, "f", new Random().nextFloat());
        ReflectionUtil.setValue(packet, "g", new Random().nextFloat());
        ReflectionUtil.setValue(packet, "h", defaultSpeed);
        ReflectionUtil.setValue(packet, "i", particleAmount);
        ReflectionUtil.sendPacket(p, packet);
    }

    public void sendToPlayer(Location l, Player p, Vector v) throws Exception {
        Object packet = Class.forName(
                "net.minecraft.server." + ReflectionUtil.getVersionString()
                        + ".PacketPlayOutWorldParticles").getConstructors()[0].newInstance();
        ReflectionUtil.setValue(packet, "a", particleName);
        ReflectionUtil.setValue(packet, "b", (float) l.getX());
        ReflectionUtil.setValue(packet, "c", (float) l.getY());
        ReflectionUtil.setValue(packet, "d", (float) l.getZ());
        ReflectionUtil.setValue(packet, "e", v.getX());
        ReflectionUtil.setValue(packet, "f", v.getY());
        ReflectionUtil.setValue(packet, "g", v.getZ());
        ReflectionUtil.setValue(packet, "h", defaultSpeed);
        ReflectionUtil.setValue(packet, "i", particleAmount);
        ReflectionUtil.sendPacket(p, packet);
    }

    public void sendDataParticle(World world, double x, double y, double z, int blockId, int blockMeta) throws Exception {
        Object packet = Class.forName(
                "net.minecraft.server." + ReflectionUtil.getVersionString()
                        + ".PacketPlayOutWorldParticles").getConstructors()[0].newInstance();
        ReflectionUtil.setValue(packet, "a", particleName + "_" + blockId + "_" + blockMeta);
        ReflectionUtil.setValue(packet, "b", (float) x);
        ReflectionUtil.setValue(packet, "c", (float) y);
        ReflectionUtil.setValue(packet, "d", (float) z);
        ReflectionUtil.setValue(packet, "e", new Random().nextFloat());
        ReflectionUtil.setValue(packet, "f", new Random().nextFloat());
        ReflectionUtil.setValue(packet, "g", new Random().nextFloat());
        ReflectionUtil.setValue(packet, "h", defaultSpeed);
        ReflectionUtil.setValue(packet, "i", particleAmount);
        ReflectionUtil.sendPacket(new Location(world, x, y, z), packet);
    }

    public void sendDataParticle(World world, double x, double y, double z, int blockId, int blockMeta, Vector v) throws Exception {
        Object packet = Class.forName(
                "net.minecraft.server." + ReflectionUtil.getVersionString()
                        + ".PacketPlayOutWorldParticles").getConstructors()[0].newInstance();
        ReflectionUtil.setValue(packet, "a", particleName + "_" + blockId + "_" + blockMeta);
        ReflectionUtil.setValue(packet, "b", (float) x);
        ReflectionUtil.setValue(packet, "c", (float) y);
        ReflectionUtil.setValue(packet, "d", (float) z);
        ReflectionUtil.setValue(packet, "e", v.getX());
        ReflectionUtil.setValue(packet, "f", v.getY());
        ReflectionUtil.setValue(packet, "g", v.getZ());
        ReflectionUtil.setValue(packet, "h", defaultSpeed);
        ReflectionUtil.setValue(packet, "i", particleAmount);
        ReflectionUtil.sendPacket(new Location(world, x, y, z), packet);
    }

    public void sendDataParticle(Location l, int blockId, int blockMeta) throws Exception {
        this.sendDataParticle(l.getWorld(), l.getX(), l.getY(), l.getZ(), blockId, blockMeta);
    }

    public void sendDataParticle(Location l, int blockId, int blockMeta, Vector v) throws Exception {
        this.sendDataParticle(l.getWorld(), l.getX(), l.getY(), l.getZ(), blockId, blockMeta, v);
    }
}