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

import com.dsh105.echopet.api.entity.attribute.AttributeManager;
import com.dsh105.echopet.api.entity.attribute.EntityAttribute;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.util.StringForm;
import com.dsh105.interact.Interact;
import com.dsh105.interact.api.Inventory;

import java.util.List;

public final class ViewMenu {

    public static Inventory<?> getInventory(List<Pet> pets) {
        Inventory.Builder inventory = Interact.inventory().size(pets.size());
        
        int slot = 0;
        for (Pet pet : pets) {
            inventory.at(Interact.position().slot(slot++).icon(pet.getType().getIcon().builder().command("pet uuid " + pet.getPetId().toString()).lore(getHoverInfo(pet).split("\\n"))));
        }
        return inventory.build();
    }
    
    public static String getHoverInfo(Pet pet) {
        StringBuilder dataBuilder = new StringBuilder();
        List<EntityAttribute> activeData = AttributeManager.getModifier(pet).getValidAttributes();
        if (!activeData.isEmpty()) {
            dataBuilder.append("{c1}Valid data types: ");
            for (EntityAttribute attribute : activeData) {
                if (dataBuilder.length() >= 35) {
                    dataBuilder.append("\n");
                }
                dataBuilder.append("{c2}").append(StringForm.create(attribute).getCaptalisedName()).append("{c1}, ");
            }
        }
        return dataBuilder.substring(0, dataBuilder.length() - 2);
    }
}