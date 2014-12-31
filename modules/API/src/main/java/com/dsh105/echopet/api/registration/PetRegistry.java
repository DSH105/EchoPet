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

package com.dsh105.echopet.api.registration;

import com.captainbern.minecraft.reflection.MinecraftReflection;
import com.captainbern.reflection.ClassTemplate;
import com.captainbern.reflection.Reflection;
import com.captainbern.reflection.SafeField;
import com.dsh105.commodus.Affirm;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.pet.Pet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

import static com.captainbern.reflection.matcher.Matchers.withType;

/**
 * Reversible registration of entities to Minecraft internals. Allows for temporary modification of internal mappings
 * so
 * that custom pet entities can be spawned.
 */
public class PetRegistry {

    private static final EntityMapModifier<Class<?>, String> CLASS_TO_NAME_MODIFIER;
    private static final EntityMapModifier<Class<?>, Integer> CLASS_TO_ID_MODIFIER;
    private static final EntityMapModifier<Integer, Class<?>> ID_TO_CLASS_MODIFIER;

    static {
        ClassTemplate entityTypesTemplate = new Reflection().reflect(MinecraftReflection.getMinecraftClass("EntityTypes"));
        List<SafeField<Map>> fields = entityTypesTemplate.getSafeFields(withType(Map.class));
        CLASS_TO_NAME_MODIFIER = new EntityMapModifier<>((Map<Class<?>, String>) fields.get(1).getAccessor().getStatic());
        ID_TO_CLASS_MODIFIER = new EntityMapModifier<>((Map<Integer, Class<?>>) fields.get(2).getAccessor().getStatic());
        CLASS_TO_ID_MODIFIER = new EntityMapModifier<>((Map<Class<?>, Integer>) fields.get(3).getAccessor().getStatic());
    }

    private final Map<PetType, PetRegistrationEntry> registrationEntries = new HashMap<>();

    public PetRegistry() {
        for (PetType petType : PetType.values()) {
            PetRegistrationEntry registrationEntry = PetRegistrationEntry.create(petType);
            registrationEntries.put(petType, registrationEntry);

            // Since these are guaranteed to be unique (for vanilla Minecraft), we can safely assume that they can be applied permanently during the lifetime of this plugin
            CLASS_TO_NAME_MODIFIER.modify(registrationEntry.getEntityClass(), registrationEntry.getName());
            CLASS_TO_ID_MODIFIER.modify(registrationEntry.getEntityClass(), registrationEntry.getRegistrationId());
        }
        CLASS_TO_NAME_MODIFIER.applyModifications();
        CLASS_TO_ID_MODIFIER.applyModifications();
    }

    public PetRegistrationEntry getRegistrationEntry(PetType petType) {
        return registrationEntries.get(petType);
    }

    public void shutdown() {
        CLASS_TO_NAME_MODIFIER.removeModifications();
        CLASS_TO_ID_MODIFIER.removeModifications();
    }

    public Pet spawn(PetType petType, final UUID playerUID) {
        Affirm.notNull(petType, "Pet type must not be null.");
        Affirm.notNull(playerUID, "Player ID must not be null.");

        final PetRegistrationEntry registrationEntry = getRegistrationEntry(petType);
        if (registrationEntry == null) {
            // Pet type not registered
            return null;
        }

        return performRegistration(registrationEntry, new Callable<Pet>() {
            @Override
            public Pet call() throws Exception {
                return registrationEntry.createFor(playerUID);
            }
        });
    }

    public <T> T performRegistration(PetRegistrationEntry registrationEntry, Callable<T> callable) {
        Class<?> existingEntityClass = ID_TO_CLASS_MODIFIER.getMap().get(registrationEntry.getRegistrationId());
        // Just to be sure, remove any existing mappings and replace them afterwards
        // Make this entity the 'default' while the pet is being spawned
        ID_TO_CLASS_MODIFIER.clear(existingEntityClass);
        ID_TO_CLASS_MODIFIER.modify(registrationEntry.getRegistrationId(), registrationEntry.getEntityClass());

        try {
            ID_TO_CLASS_MODIFIER.applyModifications();
            return callable.call();
        } catch (Exception e) {
            throw new PetRegistrationException(e);
        } finally {
            // Ensure everything is back to normal
            // Client will now receive the correct entity ID and we're all set!
            ID_TO_CLASS_MODIFIER.removeModifications();
            ID_TO_CLASS_MODIFIER.add(existingEntityClass);
        }
    }
}