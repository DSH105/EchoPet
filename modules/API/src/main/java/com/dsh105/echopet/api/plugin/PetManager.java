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

package com.dsh105.echopet.api.plugin;

import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.pet.Pet;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PetManager {

    List<Pet> getPetsOfType(PetType petType);

    List<Pet> getPetsFor(UUID playerUID);

    void mapPetName(Pet pet);

    void unmapPetNames(UUID playerUID);

    void unmapPetName(UUID playerUID, String name);

    void updatePetNameMap(UUID playerUID);

    Map<String, UUID> getPetNameMapFor(UUID playerUID);

    Pet getPetByName(UUID playerUID, String petName);

    Map<UUID, Pet> getPetUniqueIdMap();

    Pet getPetById(UUID uniqueId);

    List<Pet> getAllPets();

    void removeAllPets();

    void removePets(UUID playerUID);

    void removePet(Pet pet);

    void removePet(Pet pet, boolean makeDeathSound);

    void load(UUID playerUID, boolean sendMessage);

    void load(UUID playerUID, boolean sendMessage, LoadCallback<List<Pet>> callback);

    Pet loadRider(Pet pet);

    Pet create(UUID playerUID, PetType type, boolean sendFailMessage);

    void loadPets(UUID playerUID);

    void loadPets(UUID playerUID, LoadCallback<List<Pet>> callback);

    void loadPet(UUID playerUID, UUID petUniqueId, LoadCallback<Pet> callback);

    void forceData(Pet pet);

    void save(Pet pet);

    void clear(Pet pet);

    void clear(UUID playerUID);
}