/*
 * This file is part of Commodus.
 *
 * Commodus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Commodus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Commodus.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.bridge.container;

import com.dsh105.commodus.ServerBrand;
import com.dsh105.commodus.container.Container;

public class EventContainer extends Container<EventContainer> {

    private Object event;
    private ServerBrand serverBrand;

    private EventContainer(Object event, ServerBrand serverBrand) {
        this.event = event;
        this.serverBrand = serverBrand;
    }

    public static EventContainer from(org.bukkit.event.Event bukkitEvent) {
        return new EventContainer(bukkitEvent, ServerBrand.BUKKIT);
    }

    public static EventContainer from(org.spongepowered.api.util.event.Event spongeEvent) {
        return new EventContainer(spongeEvent, ServerBrand.SPONGE);
    }

    public org.bukkit.event.Event asBukkit() {
        if (serverBrand != ServerBrand.BUKKIT) {
            throw new IllegalStateException("Event wrapped is not a Bukkit event.");
        }
        return (org.bukkit.event.Event) event;
    }

    public org.spongepowered.api.util.event.Event asSponge() {
        if (serverBrand != ServerBrand.SPONGE) {
            throw new IllegalStateException("Event wrapped is not a Sponge event.");
        }
        return (org.spongepowered.api.util.event.Event) event;
    }

    public Object getEvent() {
        return event;
    }

    public ServerBrand getServerBrand() {
        return serverBrand;
    }

    public boolean isCancelled() {
        switch (serverBrand) {
            case BUKKIT:
                if (event instanceof org.bukkit.event.Cancellable) {
                    return ((org.bukkit.event.Cancellable) event).isCancelled();
                }
                break;
            case SPONGE:
                if (event instanceof org.spongepowered.api.util.event.Cancellable) {
                    return ((org.spongepowered.api.util.event.Cancellable) event).isCancelled();
                }
                break;
        }
        return false;
    }

    public void setCancelled(boolean cancel) {
        switch (serverBrand) {
            case BUKKIT:
                if (event instanceof org.bukkit.event.Cancellable) {
                    ((org.bukkit.event.Cancellable) event).setCancelled(cancel);
                }
                break;
            case SPONGE:
                if (event instanceof org.spongepowered.api.util.event.Cancellable) {
                    ((org.spongepowered.api.util.event.Cancellable) event).setCancelled(cancel);
                }
                break;
        }
    }
}