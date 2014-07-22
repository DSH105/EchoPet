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

import com.dsh105.echopetv3.api.entity.PetData;
import com.dsh105.echopetv3.api.entity.PetType;
import com.dsh105.echopetv3.api.entity.pet.Pet;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface SQLPetManager extends PetManager {

    void save(Pet pet, boolean isRider);

    void save(String petUniqueId, String playerIdent, PetType petType, String petName, List<PetData> petData, boolean isRider);

    List<Pet> load(String playerIdent);

    Pet load(String petUniqueId, String playerIdent);

    void clearRider(Pet pet);

    void clearRider(String petUniqueId, Player player);

    void clearRider(String petUniqueId, String playerIdent);
}