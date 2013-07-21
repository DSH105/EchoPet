package com.github.dsh105.echopet.util;

import java.util.Random;

import net.minecraft.server.v1_6_R2.Packet63WorldParticles;

import org.bukkit.Location;

public enum Particle {
	
	SMOKE("largesmoke", 0.2f, 20, "smoke", false),
	RED_SMOKE("reddust", 0f, 40, "red_smoke", false),
	RAINBOW_SMOKE("reddust", 1f, 100, "rainbow_smoke", false),
	FIRE("flame", 0.05f, 100, "fire", false),
	HEART("heart", 0f, 4, "heart", false),
	MAGIC_RUNES("enchantmenttable", 1f, 100, "magic_runes", false),
	LAVA_SPARK("lava", 0f, 4, "lava_spark", false),
	SPLASH("splash", 1f, 40, "splash", false),
	PORTAL("portal", 1f, 100, "portal", false),
	
	CLOUD("explode", 0.1f, 10, "death_cloud", false),
	CRITICAL("crit", 0.1f, 100, "critical", false),
	MAGIC_CRITIAL("magicCrit", 0.1f, 100, "magic_critical", false),
	ANGRY_VILLAGER("angryVillager", 0f, 20, "angry_sparkle", false),
	SPARKLE("happyVillager", 0f, 20, "sparkle", false),
	WATER_DRIP("dripWater", 0f, 100, "water_drip", false),
	LAVA_DRIP("dripLava", 0f, 100, "lava_drip", false),
	WITCH_MAGIC("witchMagic", 1f, 20, "witch_magic", false),
	
	SNOWBALL("snowballpoof", 1f, 20, "snowball", false),
	SNOW_SHOVEL("snowshovel", 0.02f, 30, "snow", false),
	SLIME_SPLAT("slime", 1f, 30, "slime", false),
	BUBBLE("bubble", 0f, 50, "bubble", false),
	SPELL_AMBIENT("mobSpellAmbient", 1f, 100, "spellAmbient", false),
	VOID("townaura", 1f, 100, "void", false);

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
	private String configName;
	public boolean requiresData;
	Particle(String particleName, float defaultSpeed, int particleAmount, String configName, boolean requiresData) {
		this.particleName = particleName;
		this.defaultSpeed = defaultSpeed;
		this.particleAmount = particleAmount;
		this.configName = configName;
		this.requiresData = requiresData;
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
	public String getConfigName() {
		return this.configName;
	}
	public Particle getParticleByConfigName(String s) {
		for (Particle sp : Particle.values()) {
			if (sp.getConfigName().equalsIgnoreCase(s)) {
				return sp;
			}
		}
		return null;
	}
	public void sendToLocation(Location l) throws Exception {
		Object packet = Class.forName(
				"net.minecraft.server." + ReflectionUtil.getVersionString()
					+ ".Packet63WorldParticles").getConstructors()[0].newInstance();
		ReflectionUtil.setValue(packet, "a", particleName);
		ReflectionUtil.setValue(packet, "b", (float) l.getX());
		ReflectionUtil.setValue(packet, "c", (float) l.getY());
		ReflectionUtil.setValue(packet, "d", (float) l.getZ());
		ReflectionUtil.setValue(packet, "e", new Random().nextFloat());
		ReflectionUtil.setValue(packet, "f", new Random().nextFloat());
		ReflectionUtil.setValue(packet, "g", new Random().nextFloat());
		ReflectionUtil.setValue(packet, "h", defaultSpeed);
		ReflectionUtil.setValue(packet, "i", particleAmount);
		ReflectionUtil.sendPacketToLocation(l, packet);
	}
	
	public void sendToLocation(Packet63WorldParticles packet, Location l) throws Exception {
		ReflectionUtil.setValue(packet, "a", particleName);
		ReflectionUtil.setValue(packet, "b", (float) l.getX());
		ReflectionUtil.setValue(packet, "c", (float) l.getY());
		ReflectionUtil.setValue(packet, "d", (float) l.getZ());
		ReflectionUtil.setValue(packet, "e", new Random().nextFloat());
		ReflectionUtil.setValue(packet, "f", new Random().nextFloat());
		ReflectionUtil.setValue(packet, "g", new Random().nextFloat());
		ReflectionUtil.setValue(packet, "h", defaultSpeed);
		ReflectionUtil.setValue(packet, "i", particleAmount);
		ReflectionUtil.sendPacketToLocation(l, packet);
	}
}