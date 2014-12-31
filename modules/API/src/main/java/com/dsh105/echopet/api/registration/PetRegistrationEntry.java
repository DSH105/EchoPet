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
import com.captainbern.reflection.Reflection;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.entitypet.EntityPet;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.bridge.entity.LivingEntityBridge;

import java.util.UUID;

public class PetRegistrationEntry {

    private String name;
    private int registrationId;
    private Class<? extends Pet> petClass;
    private Class<? extends EntityPet> entityClass;

    public PetRegistrationEntry(String name, int registrationId, Class<? extends Pet> petClass, Class<? extends EntityPet> entityClass) {
        if (this.entityClass == null) {
            throw new PetRegistrationException("Entity class not available");
        }
        this.name = name;
        this.registrationId = registrationId;
        this.entityClass = entityClass;
        this.petClass = petClass;
    }

    public String getName() {
        return name;
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public Class<? extends Pet> getPetClass() {
        return petClass;
    }

    public Class<? extends EntityPet> getEntityClass() {
        return entityClass;
    }

    public Pet createFor(UUID playerUID) {
        return new Reflection().reflect(this.petClass).getSafeConstructor(UUID.class).getAccessor().invoke(playerUID);
    }

    // FIXME: support Sponge/Forge
    public <T extends LivingEntityBridge, S extends EntityPet> S createEntityPet(Object nmsWorld, Pet<T, S> pet) {
        return (S) new Reflection().reflect(this.entityClass).getSafeConstructor(MinecraftReflection.getMinecraftClass("World"), this.petClass.getInterfaces()[0]).getAccessor().invoke(nmsWorld, pet);
    }

    public static PetRegistrationEntry create(PetType petType) {
        return new PetRegistrationEntry(petType.humanName() + "-Pet", petType.getRegistrationId(), petType.getPetClass(), petType.getEntityClass());
    }
}