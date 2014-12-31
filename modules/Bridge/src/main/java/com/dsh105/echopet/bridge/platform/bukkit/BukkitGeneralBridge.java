package com.dsh105.echopet.bridge.platform.bukkit;

import com.dsh105.commodus.Affirm;
import com.dsh105.echopet.bridge.GeneralBridge;
import com.dsh105.echopet.bridge.container.EventContainer;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public class BukkitGeneralBridge implements GeneralBridge {

    @Override
    public void postEvent(EventContainer event) {
        Bukkit.getServer().getPluginManager().callEvent(event.asBukkit());
    }
}