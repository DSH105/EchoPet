package io.github.dsh105.echopet;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.plugin.Plugin;
import org.kitteh.vanish.VanishPlugin;

/**
 * Project by DSH105
 */

public class Hook {

    public static VanishPlugin getVNP() {
        Plugin plugin = EchoPet.getPluginInstance().getServer().getPluginManager().getPlugin("VanishNoPacket");
        if (plugin == null || !(plugin instanceof VanishPlugin)) {
            return null;
        }
        return (VanishPlugin) plugin;
    }

    public static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = EchoPet.getPluginInstance().getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }
}