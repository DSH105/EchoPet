package com.github.dsh105.echopet.entity.pet;

import com.github.dsh105.echopet.EchoPet;

public enum SizeCategory {
	
	TINY(1),
	REGULAR(1),
	LARGE(3),
	GIANT(4);
	
	private int mod;
	private float start;
	private float stop;
	private float tele;
	SizeCategory(int modifier) {
		this.mod = modifier;
		start = (Integer) EchoPet.getPluginInstance().DO.getConfigOption("startWalkDistance", 12);
		stop = (Integer) EchoPet.getPluginInstance().DO.getConfigOption("stopWalkDistance", 8);
		tele = (Integer) EchoPet.getPluginInstance().DO.getConfigOption("teleportDistance", 30);
	}
	
	public float getStartWalk() {
		return this.start * this.mod;
	}
	
	public float getStopWalk() {
		return this.stop * this.mod;
	}
	
	public float getTeleport() {
		return this.tele * this.mod;
	}
}