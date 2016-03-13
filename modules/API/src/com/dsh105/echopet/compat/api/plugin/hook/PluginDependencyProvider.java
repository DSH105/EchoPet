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

package com.dsh105.echopet.compat.api.plugin.hook;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import com.dsh105.echopet.compat.api.plugin.EchoPet;

/**
 * Needs some optimization
 */
public abstract class PluginDependencyProvider<T extends Plugin> implements IPluginDependencyProvider<T> {

    protected PluginDependencyProvider<T> instance;
    private T dependency;
    protected boolean hooked;
    private Plugin myPluginInstance;
    private String dependencyName;

    // TODO: add more utils, plugin stuff mostly.

    public PluginDependencyProvider(Plugin myPluginInstance, String dependencyName) {
        this.instance = this;
        this.myPluginInstance = myPluginInstance;
        this.dependencyName = dependencyName;

        if (dependency == null && !this.hooked) {
            try {
                dependency = (T) Bukkit.getPluginManager().getPlugin(getDependencyName());

                if (this.dependency != null && this.dependency.isEnabled()) {
                    onHook();
                    this.hooked = true;
                    EchoPet.LOG.info("[" + this.dependency.getName() + "] Successfully hooked");
                }
            } catch (Exception e) {
                EchoPet.LOG.warning("Could not create a PluginDependencyProvider for: " + getDependencyName() + "! (Are you sure the type is valid?) - " + e.getMessage());
            }
        }

        Bukkit.getPluginManager().registerEvents(new Listener() {

            @EventHandler
            protected void onEnable(PluginEnableEvent event) {
                if ((dependency == null) && (event.getPlugin().getName().equalsIgnoreCase(getDependencyName()))) {
                    try {
                        dependency = (T) event.getPlugin();
                        onHook();
                        hooked = true;
                        EchoPet.LOG.info("[" + getDependencyName() + "] Successfully hooked");
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to hook plugin: " + event.getPlugin().getName());
                    }
                }
            }

            @EventHandler
            protected void onDisable(PluginDisableEvent event) {
                if ((dependency != null) && (event.getPlugin().getName().equalsIgnoreCase(getDependencyName()))) {
                    dependency = null;
                    onUnhook();
                    hooked = false;
                    EchoPet.LOG.info("[" + getDependencyName() + "] Successfully unhooked");
                }
            }
        }, getHandlingPlugin());
    }

    public abstract void onHook();

    public abstract void onUnhook();


    public T getDependency() {
        if (this.dependency == null) {
            throw new RuntimeException("Dependency is NULL!");
        }
        return this.dependency;
    }


    public boolean isHooked() {
        return this.hooked;
    }


    public Plugin getHandlingPlugin() {
        if (this.myPluginInstance == null) {
            throw new RuntimeException("HandlingPlugin is NULL!");
        }
        return this.myPluginInstance;
    }


    public String getDependencyName() {
        if (this.dependencyName == null) {
            throw new RuntimeException("Dependency name is NULL!");
        }
        return this.dependencyName;
    }
}
