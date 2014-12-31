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

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.echopet.api.configuration.ConfigType;
import com.dsh105.echopet.api.configuration.MenuSettings;
import com.dsh105.echopet.api.configuration.PetSettings;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.interact.Interact;
import com.dsh105.interact.api.CommandIcon;
import com.dsh105.interact.api.Icon;
import com.dsh105.interact.api.Inventory;

import java.util.List;
import java.util.Map;

public final class PetSelector {

    private static Inventory<?> DEFAULT;
    private static Inventory<?> INVENTORY;
    
    static {
        getDefault();
    }

    public static Inventory<?> getDefault() {
        if (DEFAULT == null) {
            int size = 45;
            Inventory.Builder builder = Interact.inventory().size(size);

            List<PetType> petTypes = PetType.sortAlphabetically();
            for (int i = 0; i < petTypes.size(); i++) {
                PetType type = petTypes.get(i);
                builder.at(Interact.position().slot(i).icon(type.getIcon()));
            }

            MenuPreset[] presets = {MenuPreset.CLOSE_SELECTOR, MenuPreset.TOGGLE, MenuPreset.CALL, MenuPreset.HAT, MenuPreset.RIDE, MenuPreset.NAME, MenuPreset.MENU};
            int[] diffs = {1, 3, 4, 6, 7, 8, 9};
            for (int i = 0; i < presets.length; i++) {
                builder.at(Interact.position().slot(size - diffs[i]).icon(presets[i].getIcon()));
            }

            DEFAULT = builder.build();
        }
        
        return DEFAULT;
    }
    
    public static Inventory<?> getInventory() {
        if (INVENTORY == null) {
            try {
                INVENTORY = Interact.inventory().from((Map<String, Object>) EchoPet.getConfig(ConfigType.MENU).getMapList("selector")).build();
            } catch (Exception e) {
                // copy it over
                INVENTORY = getDefault().builder().build();
            }
            
            for (Map.Entry<Integer, Icon> entry : INVENTORY.getLayout().getIcons().entrySet()) {
                if (entry.getValue() instanceof CommandIcon) {
                    CommandIcon cIcon = (CommandIcon) entry.getValue();
                    if (!MenuSettings.SELECTOR_SHOW_DISABLED_PETS.getValue()) {
                        String[] parts = cIcon.getCommand().split("\\s+");
                        if (parts.length <= 1 || !GeneralUtil.isEnumType(PetType.class, parts[1])) {
                            continue;
                        }
                        if (!PetSettings.ENABLE.getValue(GeneralUtil.toEnumType(PetType.class, parts[1]).storageName())) {
                            INVENTORY.getLayout().remove(entry.getKey());
                        }
                    }
                }
            }
        }
        
        return INVENTORY;
    }
    
    public static void reset() {
        INVENTORY = null;
        getInventory();
    }
}