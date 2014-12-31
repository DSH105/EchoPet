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
import com.dsh105.echopet.api.entity.attribute.EntityAttribute;
import com.dsh105.echopet.api.entity.pet.Pet;

import java.util.List;
import java.util.UUID;

public interface SQLPetManager extends PetManager {

    void save(Pet pet, boolean isRider);

    void save(UUID petUniqueId, UUID playerUID, PetType petType, String petName, List<EntityAttribute> petData, boolean isRider);

    @Override
    void loadPets(UUID playerUID);

    @Override
    void loadPets(UUID playerUID, LoadCallback<List<Pet>> callback);

    void clearRider(Pet pet);

    void clearRider(UUID petUniqueId);
}