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
import com.dsh105.echopet.api.plugin.EchoPet;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class PetRegistrationEntry {

    private String name;
    private int registrationId;
    private Class<? extends Pet> petClass;
    private Class<? extends EntityPet> entityClass;

    public PetRegistrationEntry(String name, int registrationId, String classIdentifier) {
        this.name = name;
        this.registrationId = registrationId;
        this.entityClass = new Reflection().reflect(EchoPet.INTERNAL_NMS_PATH + ".entity.type.Entity" + classIdentifier + "PetBase").getReflectedClass();
        this.petClass = new Reflection().reflect("com.dsh105.echopet.api.entity.pet.type." + classIdentifier + "PetBase").getReflectedClass();
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

    public Pet createFor(Player owner) {
        return new Reflection().reflect(this.petClass).getSafeConstructor(Player.class).getAccessor().invoke(owner);
    }

    public <T extends LivingEntity, S extends EntityPet> S createEntityPet(Object nmsWorld, Pet<T, S> pet) {
        return (S) new Reflection().reflect(this.entityClass).getSafeConstructor(MinecraftReflection.getMinecraftClass("World"), this.petClass.getInterfaces()[0]).getAccessor().invoke(nmsWorld, pet);
    }

    public static PetRegistrationEntry create(PetType petType) {
        return new PetRegistrationEntry(petType.humanName() + "-Pet", petType.getRegistrationId(), petType.humanName().replaceAll("\\s+", ""));
    }
}