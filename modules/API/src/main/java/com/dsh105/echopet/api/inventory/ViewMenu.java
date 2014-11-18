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

package com.dsh105.echopet.api.inventory;

import com.dsh105.commodus.StringUtil;
import com.dsh105.commodus.particle.Particle;
import com.dsh105.echopet.api.config.ConfigType;
import com.dsh105.echopet.api.entity.AttributeManager;
import com.dsh105.echopet.api.entity.PetData;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.menuapi.api.*;
import com.dsh105.powermessage.core.PowerMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewMenu {

    public static Menu prepare(List<Pet> pets) {
        // TODO: configurable?
        Menu menu = new Menu(EchoPet.getCore(), "Active Pets", pets.size());
        int i = 0;
        for (Pet pet : pets) {
            // TODO
            menu.setSlot(i++, new CommandIcon("pet uuid " + pet.getPetId().toString(), pet.getType().getMaterial(), 1, pet.getType().getMaterialData(), pet.getName(), getHoverInfo(pet).split("\\n")));
        }
        return menu;
    }

    public static void displayInfo(Pet pet) {
        new PowerMessage(ChatColor.WHITE + "• {c1}" + pet.getType().humanName() + " ({c2}" + pet.getName() + "{c1})")
                .tooltip(getHoverInfo(pet))
                .send(pet.getOwner());
    }

    public static String getHoverInfo(Pet pet) {
        StringBuilder dataBuilder = new StringBuilder();
        List<PetData> activeData = AttributeManager.getModifier(pet).getActiveDataValues(pet);
        if (!activeData.isEmpty()) {
            dataBuilder.append("{c1}Valid data types: ");
            for (PetData data : activeData) {
                if (dataBuilder.length() >= 35) {
                    dataBuilder.append("\n");
                }
                dataBuilder.append("{c2}").append(data.humanName()).append("{c1}, ");
            }
        }
        return dataBuilder.substring(0, dataBuilder.length() - 2);
    }
}