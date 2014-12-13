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

import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.compat.api.entity.IEntityPet;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.util.ReflectionUtil;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PetRegistrationEntry {

    private String name;
    private int registrationId;
    private Class<? extends IPet> petClass;
    private Class<? extends IEntityPet> entityClass;

    private Constructor<? extends IPet> petConstructor;
    private Constructor<? extends IEntityPet> entityPetConstructor;

    public PetRegistrationEntry(String name, int registrationId, Class<? extends IPet> petClass, Class<? extends IEntityPet> entityClass) {
        if (this.entityClass == null) {
            throw new PetRegistrationException("Pet type is not supported by this server version.");
        }

        this.name = name;
        this.registrationId = registrationId;
        this.entityClass = entityClass;
        this.petClass = petClass;

        try {
            this.petConstructor = this.petClass.getConstructor(Player.class);
            this.entityPetConstructor = this.entityClass.getConstructor(ReflectionUtil.getNMSClass("World"), IPet.class);
        } catch (NoSuchMethodException e) {
            throw new PetRegistrationException("Failed to create pet constructors!", e);
        }
    }

    public String getName() {
        return name;
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public Class<? extends IPet> getPetClass() {
        return petClass;
    }

    public Class<? extends IEntityPet> getEntityClass() {
        return entityClass;
    }

    public IPet createFor(Player owner) {
        Throwable throwable;
        try {
            return this.petConstructor.newInstance(owner);
        } catch (InstantiationException e) {
            throwable = e;
        } catch (IllegalAccessException e) {
            throwable = e;
        } catch (InvocationTargetException e) {
            throwable = e;
        }
        throw new IllegalStateException("Failed to create pet object for " + owner.getName(), throwable);
    }

    public IEntityPet createEntityPet(Object nmsWorld, IPet pet) {
        Throwable throwable;
        try {
            return this.entityPetConstructor.newInstance(nmsWorld, pet);
        } catch (InstantiationException e) {
            throwable = e;
        } catch (IllegalAccessException e) {
            throwable = e;
        } catch (InvocationTargetException e) {
            throwable = e;
        }
        throw new IllegalStateException("Failed to create EntityPet object for " + pet.getOwner().getName(), throwable);
    }

    public static PetRegistrationEntry create(PetType petType) {
        return new PetRegistrationEntry(StringUtil.capitalise(petType.toString().toLowerCase().replace("_", " ")).replace(" ", "") + "-Pet", petType.getRegistrationId(), petType.getPetClass(), petType.getEntityClass());
    }
}