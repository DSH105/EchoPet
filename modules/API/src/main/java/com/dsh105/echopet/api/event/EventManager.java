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

package com.dsh105.echopet.api.event;

import com.dsh105.echopet.bridge.container.EventContainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class EventManager {

    private final Map<Object, Map<Class<?>, Method>> listeners = new HashMap<>();

    public void register(Object listener) {
        Map<Class<?>, Method> events = listeners.get(listener);
        if (events == null) {
            events = new HashMap<>();
        }

        // Only search for methods in the one class
        // Searching super classes/interfaces is not worth it for the use cases
        for (Method method : listener.getClass().getMethods()) {
            Listen listen = method.getAnnotation(Listen.class);
            if (listen != null) {
                for (Class<? extends org.bukkit.event.Event> bukkitEvent : listen.bukkit()) {
                    if (!bukkitEvent.equals(NullBukkitEvent.class)) {
                        events.put(bukkitEvent, method);
                    }
                }

                for (Class<? extends org.spongepowered.api.util.event.Event> spongeEvent : listen.sponge()) {
                    if (!spongeEvent.equals(NullSpongeEvent.class)) {
                        events.put(spongeEvent, method);
                    }
                }
            }
        }

        listeners.put(listener, events);
    }

    public void post(EventContainer event) {
        try {
            for (Object listener : listeners.keySet()) {
                for (Map.Entry<Class<?>, Method> entry : listeners.get(listener).entrySet()) {
                    if (entry.getKey().isAssignableFrom(event.getEvent().getClass())) {
                        entry.getValue().invoke(listener, event);
                    }
                }
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new IllegalStateException("Failed to access event listener", e);
        } finally {
            // Clear all contextual values provided by the container
            event.clear();
        }
    }
}