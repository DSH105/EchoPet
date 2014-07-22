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

package com.dsh105.echopetv3.api.plugin;

import com.dsh105.echopetv3.api.entity.PetType;
import com.dsh105.echopetv3.api.entity.pet.Pet;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PetManager {

    List<Pet> getPetsOfType(PetType petType);

    List<Pet> getPetsFor(String playerIdent);

    List<Pet> getPetsFor(Player player);

    Map<String, Pet> getPetNameMapFor(String playerIdent);

    Map<String, Pet> getPetNameMapFor(Player player);

    Pet getPetByName(String playerIdent, String petName);

    Pet getPetByName(Player player, String petName);

    Map<UUID, Pet> getPetUniqueIdMap();

    Pet getPetById(UUID uniqueId);

    String getStorageNameOf(Pet pet);

    List<Pet> getAllPets();

    void removeAllPets();

    void removePets(String playerIdent);

    void removePets(Player player);

    void removePet(Pet pet);

    void removePet(Pet pet, boolean makeDeathSound);

    List<Pet> load(Player player, boolean sendMessage);

    Pet loadRider(Pet pet);

    Pet create(Player owner, PetType type, boolean sendFailMessage);

    List<Pet> loadPets(Player owner);

    Pet loadPet(Player owner, String petStorageName);

    void forceData(Pet pet);

    void save(Pet pet);

    void clear(Pet pet);

    void clear(Player player);

    void clear(String playerIdent);
}