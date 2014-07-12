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

package com.dsh105.echopetv3.api.inventory;

import com.dsh105.commodus.StringUtil;
import com.dsh105.commodus.particle.Particle;
import com.dsh105.echopetv3.api.entity.PetData;
import com.dsh105.echopetv3.api.entity.PetType;
import com.dsh105.echopetv3.api.entity.pet.Pet;
import com.dsh105.echopetv3.api.plugin.EchoPet;
import com.dsh105.menuapi.api.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataMenu {

    private static HashMap<PetType, Layout> LAYOUTS = new HashMap<>();

    public static Layout getDefaultLayout(PetType petType) {
        ArrayList<MenuPreset> presets = MenuPreset.getPresets(petType);
        Layout layout = new Layout(presets.size(), petType.humanName() + " - Data");

        for (int i = 0; i < presets.size(); i++) {
            MenuPreset preset = presets.get(i);
            Icon icon = new Icon(preset.getMaterial(), preset.getAmount(), preset.getMaterialData(), preset.getName());
            if (preset.getCommand() != null && !preset.getCommand().isEmpty()) {
                icon = new CommandIcon(preset.getCommand(), preset.getMaterial(), preset.getAmount(), preset.getMaterialData(), preset.getName());
            }
            layout.setSlot(i, icon);
        }

        LAYOUTS.put(petType, layout);
        return layout;
    }

    public static Menu prepare(Pet pet) {
        return getLayout(pet).toMenu(EchoPet.getCore());
    }

    public static Layout getLayout(final Pet pet) {
        Layout layout = LAYOUTS.get(pet.getType());
        if (layout == null) {
            layout = getDefaultLayout(pet.getType());
        }

        final ArrayList<MenuPreset> typePresets = MenuPreset.getPresets(pet.getType());

        HashMap<Integer, Icon> slots = layout.getSlots();
        for (Map.Entry<Integer, Icon> entry : slots.entrySet()) {
            final int slotNumber = entry.getKey();
            final Icon icon = entry.getValue();
            final MenuPreset preset = typePresets.get(slotNumber);
            final PetData petData = preset.getPetData();

            if (!(icon instanceof CommandIcon)) {
                icon.setName(icon.getName() + (pet.getActiveDataValues().contains(petData) ? ChatColor.GREEN + " [TOGGLE ON]" : ChatColor.RED + " [TOGGLE OFF]"));
                icon.setCallback(new IconCallback() {
                    @Override
                    public void run(Player viewer) {
                        if (pet != null && pet.getOwner() != null) {
                            if (petData == null) {
                                Menu menu = getSecondLevelLayout(pet, preset.getDataType()).toMenu(EchoPet.getCore());
                                menu.show(viewer);
                            } else {
                                pet.invertDataValue(petData);
                                Particle.RED_SMOKE.builder().show(pet.getLocation());
                            }
                        }
                    }
                });
            }
            layout.setSlot(slotNumber, icon);
        }

        MenuPreset preset = MenuPreset.CLOSE_DATA;
        layout.setSlot(layout.getSize() - 1, new Icon(preset.getMaterial(), preset.getAmount(), preset.getMaterialData(), preset.getName()));
        return layout;
    }

    public static Layout getSecondLevelLayout(final Pet pet, PetData.Type dataType) {
        ArrayList<MenuPreset> presets = MenuPreset.getPresetsOfType(dataType);

        Layout layout = new Layout(presets.size(), pet.getType().humanName() + " - Data - " + StringUtil.capitalise(dataType.toString()));

        HashMap<Integer, Icon> slots = layout.getSlots();
        for (Map.Entry<Integer, Icon> entry : slots.entrySet()) {
            final int slotNumber = entry.getKey();
            final Icon icon = entry.getValue();
            final MenuPreset preset = presets.get(slotNumber);
            final PetData petData = preset.getPetData();

            if (!(icon instanceof CommandIcon)) {
                icon.setName(icon.getName() + (pet.getActiveDataValues().contains(petData) ? ChatColor.GREEN + " [TOGGLE ON]" : ChatColor.RED + " [TOGGLE OFF]"));
                icon.setCallback(new IconCallback() {
                    @Override
                    public void run(Player viewer) {
                        if (pet != null && pet.getOwner() != null) {
                            pet.setDataValue(petData);
                            Particle.RED_SMOKE.builder().show(pet.getLocation());
                        }
                    }
                });
            }
            layout.setSlot(slotNumber, icon);
        }

        MenuPreset preset = MenuPreset.BACK;
        layout.setSlot(layout.getSize() - 1, new Icon(preset.getMaterial(), preset.getAmount(), preset.getMaterialData(), preset.getName()) {
            @Override
            public void onClick(Player viewer) {
                Menu menu = getLayout(pet).toMenu(EchoPet.getCore());
                menu.show(viewer);
            }
        });
        return layout;
    }
}