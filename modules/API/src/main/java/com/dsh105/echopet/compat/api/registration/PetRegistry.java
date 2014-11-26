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

package com.dsh105.echopet.compat.api.registration;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.util.ReflectionUtil;
import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Reversible registration of entities to Minecraft internals. Allows for temporary modification of internal mappings
 * so
 * that custom pet entities can be spawned.
 *
 * NOTE: This class is a modified version of the registry used in EchoPet v3.
 */
public class PetRegistry {

    private static final EntityMapModifier<Class<?>, String> CLASS_TO_NAME_MODIFIER;
    private static final EntityMapModifier<Class<?>, Integer> CLASS_TO_ID_MODIFIER;

    static {
        Class<?> entityTypes = ReflectionUtil.getNMSClass("EntityTypes");
        List<Field> typeMaps = new ArrayList<Field>();
        for (Field candidate : entityTypes.getDeclaredFields()) {
            if (Map.class.isAssignableFrom(candidate.getType())) {
                candidate.setAccessible(true);
                typeMaps.add(candidate);
            }
        }
        try {
            CLASS_TO_NAME_MODIFIER = new EntityMapModifier<Class<?>, String>((Map<Class<?>, String>) typeMaps.get(1).get(null));
            CLASS_TO_ID_MODIFIER = new EntityMapModifier<Class<?>, Integer>((Map<Class<?>, Integer>) typeMaps.get(3).get(null));
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to initialise Entity type maps correctly!", e);
        }
    }

    private final Map<PetType, PetRegistrationEntry> registrationEntries = new HashMap<PetType, PetRegistrationEntry>();

    public PetRegistry() {
        for (PetType petType : PetType.values()) {
            registrationEntries.put(petType, PetRegistrationEntry.create(petType));
        }
    }

    public PetRegistrationEntry getRegistrationEntry(PetType petType) {
        return registrationEntries.get(petType);
    }

    public IPet spawn(PetType petType, final Player owner) {
        Preconditions.checkNotNull(petType, "Pet type must not be null.");
        Preconditions.checkNotNull(owner, "Owner type must not be null.");

        final PetRegistrationEntry registrationEntry = getRegistrationEntry(petType);
        if (registrationEntry == null) {
            // Pet type not registered
            return null;
        }

        return performRegistration(registrationEntry, new Callable<IPet>() {
            @Override
            public IPet call() throws Exception {
                return registrationEntry.createFor(owner);
            }
        });
    }

    public <T> T performRegistration(PetRegistrationEntry registrationEntry, Callable<T> callable) {
        // Just to be sure, remove any existing mappings and replace them afterwards
        CLASS_TO_NAME_MODIFIER.clear(registrationEntry.getName());
        CLASS_TO_ID_MODIFIER.clear(registrationEntry.getRegistrationId());
        CLASS_TO_NAME_MODIFIER.modify(registrationEntry.getEntityClass(), registrationEntry.getName());
        CLASS_TO_ID_MODIFIER.modify(registrationEntry.getEntityClass(), registrationEntry.getRegistrationId());

        try {
            CLASS_TO_NAME_MODIFIER.applyModifications();
            CLASS_TO_ID_MODIFIER.applyModifications();
            return callable.call();
        } catch (Exception e) {
            throw new PetRegistrationException(e);
        } finally {
            // Ensure everything is back to normal
            CLASS_TO_NAME_MODIFIER.removeModifications();
            CLASS_TO_ID_MODIFIER.removeModifications();
            CLASS_TO_NAME_MODIFIER.add(registrationEntry.getName());
            CLASS_TO_ID_MODIFIER.add(registrationEntry.getRegistrationId());
        }
    }
}