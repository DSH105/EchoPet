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
import com.dsh105.echopet.api.config.ConfigType;
import com.dsh105.echopet.api.config.MenuSettings;
import com.dsh105.echopet.api.config.PetSettings;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.menuapi.api.CommandIcon;
import com.dsh105.menuapi.api.Icon;
import com.dsh105.menuapi.api.Layout;
import com.dsh105.menuapi.api.Menu;

import java.util.List;
import java.util.Map;

public class PetSelector {

    private static Layout LAYOUT;

    public static Layout getDefaultLayout() {
        Layout layout = new Layout(45, "Pets", MenuPreset.SELECTOR_PRESET);

        List<PetType> types = PetType.sortAlphabetically();
        for (int i = 0; i < types.size(); i++) {
            PetType petType = types.get(i);
            layout.setSlot(i, new CommandIcon("echopet.pet.type." + petType.storageName(), petType.getCommand(), petType.getMaterial(), 1, petType.getMaterialData(), petType.humanName()));
        }

        MenuPreset[] selectorItems = new MenuPreset[]{MenuPreset.CLOSE_SELECTOR, null, MenuPreset.TOGGLE, MenuPreset.CALL, null, MenuPreset.HAT, MenuPreset.RIDE, MenuPreset.NAME, MenuPreset.MENU};
        for (int j = 1; j < 10; j++) {
            MenuPreset preset = selectorItems[j - 1];
            if (preset != null) {
                layout.setSlot(45 - j, new CommandIcon(preset.getCommand(), preset.getMaterial(), preset.getAmount(), preset.getMaterialData(), preset.getName()));
            }
        }
        return layout;
    }

    public static Layout getLayout() {
        if (LAYOUT == null) {
            LAYOUT = new Layout().loadFromFile(EchoPet.getConfig(ConfigType.MENU).config(), "selector");

            if (LAYOUT == null) {
                LAYOUT = getDefaultLayout();
            }

            for (Map.Entry<Integer, Icon> entry : LAYOUT.getSlots().entrySet()) {
                if (entry.getValue() instanceof CommandIcon) {
                    CommandIcon icon = (CommandIcon) entry.getValue();
                    if (!MenuSettings.SELECTOR_SHOW_DISABLED_PETS.getValue(EchoPet.getCore().getConfig(ConfigType.MENU).config())) {
                        String[] parts = icon.getCommand().split("\\s+");
                        if (parts.length <= 1) {
                            continue;
                        }
                        if (!GeneralUtil.isEnumType(PetType.class, parts[1].toUpperCase())) {
                            continue;
                        }
                        PetType petType = PetType.valueOf(parts[1].toUpperCase());
                        if (!PetSettings.ENABLE.getValue(petType.storageName())) {
                            LAYOUT.setSlot(entry.getKey(), null);
                        }
                    }
                }
            }
        }
        return LAYOUT;
    }

    public static void reloadLayout() {
        LAYOUT = null;
    }

    public static Menu prepare() {
        return getLayout().toMenu(EchoPet.getCore());
    }
}