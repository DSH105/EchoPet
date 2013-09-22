package com.github.dsh105.echopet.util;

import com.github.dsh105.echopet.EchoPet;

/**
 * Project by DSH105
 */

public class WorldUtil {

	public static boolean allowPets(String worldName) {
		Boolean b = EchoPet.getPluginInstance().getMainConfig().getBoolean("worlds." + worldName);
		if (b != null) {
			return b;
		}
		else {
			return EchoPet.getPluginInstance().getMainConfig().getBoolean("worlds.enableWorldsByDefault", true);
		}
	}
}