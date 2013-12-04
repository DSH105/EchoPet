package io.github.dsh105.echopet.util;

import io.github.dsh105.echopet.EchoPet;
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

    public static PacketPlayOutWorldParticles createPacket(String particleName, Location location, Vector v, float defaultSpeed, int particleAmount) {
        return new PacketPlayOutWorldParticles(
                particleName,
                (float) location.getX(),
                (float) location.getY(),
                (float) location.getZ(),
                (float) v.getX(),
                (float) v.getY(),
                (float) v.getZ(),
                defaultSpeed, particleAmount);
    }

    public void sendTo(Location l) throws Exception {
        ReflectionUtil.sendPacket(l, this.createPacket(this.particleName, l, new Vector(EchoPet.random().nextFloat(), EchoPet.random().nextFloat(), EchoPet.random().nextFloat()), this.defaultSpeed, this.particleAmount));
    }

    public void sendTo(Location l, Vector v) throws Exception {
        ReflectionUtil.sendPacket(l, this.createPacket(this.particleName, l, v, this.defaultSpeed, this.particleAmount));
    }

    public void sendToPlayer(Location l, Player p) throws Exception {
        ReflectionUtil.sendPacket(p, this.createPacket(this.particleName, l, new Vector(EchoPet.random().nextFloat(), EchoPet.random().nextFloat(), EchoPet.random().nextFloat()), this.defaultSpeed, this.particleAmount));
    }

    public void sendToPlayer(Location l, Player p, Vector v) throws Exception {
        ReflectionUtil.sendPacket(p, this.createPacket(this.particleName, l, v, this.defaultSpeed, this.particleAmount));
    }

    public void sendDataParticle(Location l, int blockId, int blockMeta) throws Exception {
        ReflectionUtil.sendPacket(l, this.createPacket(this.particleName + "_" + blockId + "_" + blockMeta, l, new Vector(EchoPet.random().nextFloat(), EchoPet.random().nextFloat(), EchoPet.random().nextFloat()), this.defaultSpeed, this.particleAmount));
    }

    public void sendDataParticle(Location l, Vector v, int blockId, int blockMeta) throws Exception {
        ReflectionUtil.sendPacket(l, this.createPacket(this.particleName + "_" + blockId + "_" + blockMeta, l, v, this.defaultSpeed, this.particleAmount));
    }

    public void sendDataParticleToPlayer(Location l, Player p, int blockId, int blockMeta) throws Exception {
        ReflectionUtil.sendPacket(p, this.createPacket(this.particleName + "_" + blockId + "_" + blockMeta, l, new Vector(EchoPet.random().nextFloat(), EchoPet.random().nextFloat(), EchoPet.random().nextFloat()), this.defaultSpeed, this.particleAmount));
    }

    public void sendDataParticleToPlayer(Location l, Player p, Vector v, int blockId, int blockMeta) throws Exception {
        ReflectionUtil.sendPacket(p, this.createPacket(this.particleName + "_" + blockId + "_" + blockMeta, l, v, this.defaultSpeed, this.particleAmount));
    }
}