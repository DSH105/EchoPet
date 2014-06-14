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

package com.dsh105.echopet.compat.api.plugin;

import com.dsh105.echopet.compat.api.entity.pet.Pet;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public interface IPetManager {

    public ArrayList<Pet> getPets();

    public Pet loadPets(Player p, boolean findDefault, boolean sendMessage, boolean checkWorldOverride);

    public void removeAllPets();

    public Pet createPet(Player owner, PetType petType, boolean sendMessageOnFail);

    public Pet createPet(Player owner, PetType petType, PetType riderType);

    public Pet getPet(Player player);

    public Pet getPet(Entity pet);

    public void forceAllValidData(Pet pet);

    public void updateFileData(String type, Pet pet, ArrayList<PetData> list, boolean b);

    public Pet createPetFromFile(String type, Player p);

    public void loadRiderFromFile(Pet pet);

    public void loadRiderFromFile(String type, Pet pet);

    public void removePets(Player player, boolean makeDeathSound);

    public void removePet(Pet pet, boolean makeDeathSound);

    public void saveFileData(String type, Pet pet);

    public void saveFileData(String type, Player p, PetStorage UPD, PetStorage UMD);

    public void saveFileData(String type, Player p, PetStorage UPD);

    public void clearAllFileData();

    public void clearFileData(String type, Pet pet);

    public void clearFileData(String type, Player p);
}