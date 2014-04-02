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

package io.github.dsh105.echopet.compat.api.plugin;

import io.github.dsh105.echopet.compat.api.entity.IPet;
import io.github.dsh105.echopet.compat.api.entity.PetData;
import io.github.dsh105.echopet.compat.api.entity.PetType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public interface IPetManager {

    public ArrayList<IPet> getPets();

    public IPet loadPets(Player p, boolean findDefault, boolean sendMessage, boolean checkWorldOverride);

    public void removeAllPets();

    public IPet createPet(Player owner, PetType petType, boolean sendMessageOnFail);

    public IPet createPet(Player owner, PetType petType, PetType riderType);

    public IPet getPet(Player player);

    public IPet getPet(Entity pet);

    public void forceAllValidData(IPet pi);

    public void updateFileData(String type, IPet pet, ArrayList<PetData> list, boolean b);

    public IPet createPetFromFile(String type, Player p);

    public void loadRiderFromFile(IPet pet);

    public void loadRiderFromFile(String type, IPet pet);

    public void removePets(String player, boolean makeDeathSound);

    public void removePet(IPet pi, boolean makeDeathSound);

    public void saveFileData(String type, IPet pi);

    public void saveFileData(String type, Player p, PetStorage UPD, PetStorage UMD);

    public void saveFileData(String type, String name, PetStorage UPD, PetStorage UMD);

    public void saveFileData(String type, Player p, PetStorage UPD);

    public void saveFileData(String type, String name, PetStorage UPD);

    public void clearAllFileData();

    public void clearFileData(String type, IPet pi);

    public void clearFileData(String type, String pName);

    public void clearFileData(String type, Player p);

    public void setData(IPet pet, PetData[] data, boolean b);

    public void setData(IPet pet, PetData pd, boolean b);
}