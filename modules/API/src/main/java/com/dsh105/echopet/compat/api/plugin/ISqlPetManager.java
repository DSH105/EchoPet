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

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;
import org.bukkit.entity.Player;

import java.util.List;

public interface ISqlPetManager {

    public void saveToDatabase(IPet p, boolean isRider);

    void saveToDatabase(String playerIdent, PetType petType, String petName, List<PetData> petData, boolean isRider);

    public IPet createPetFromDatabase(Player player);

    IPet createPetFromDatabase(String playerIdent);

    public void clearFromDatabase(Player player);

    void clearFromDatabase(String playerIdent);

    public void clearRiderFromDatabase(Player player);

    void clearRiderFromDatabase(String playerIdent);
}