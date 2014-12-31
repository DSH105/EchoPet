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

import com.dsh105.commodus.ServerUtil;
import com.dsh105.commodus.container.PlayerContainer;
import com.dsh105.commodus.particle.Particle;
import com.dsh105.echopet.api.configuration.MenuSettings;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.attribute.AttributeType;
import com.dsh105.echopet.api.entity.attribute.Attributes;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.util.StringForm;
import com.dsh105.interact.Interact;
import com.dsh105.interact.api.CommandIcon;
import com.dsh105.interact.api.Icon;
import com.dsh105.interact.api.Inventory;
import com.dsh105.interact.api.Position;
import com.dsh105.interact.api.action.ClickAction;
import com.dsh105.interact.api.action.PrepareIconAction;
import org.bukkit.ChatColor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataMenu {

    private static final Map<PetType, Inventory<?>> TYPE_DEFAULTS = new HashMap<>();
    private static final Map<PetType, Map<AttributeType, Inventory<?>>> ATTRIBUTE_TYPE_DEFAULTS = new HashMap<>();
    
    static {
        load();
    }
    
    private static void load() {
        for (PetType petType : PetType.values()) {
            List<MenuPreset> typePresets = MenuPreset.getPresets(petType);
            Inventory.Builder builder = Interact.inventory().size(typePresets.size()).name(petType.humanName() + " Attributes");

            for (int i = 0; i < typePresets.size(); i++) {
                MenuPreset preset = typePresets.get(i);
                builder.at(Interact.position().slot(i).icon(preset.getIcon()));
            }

            TYPE_DEFAULTS.put(petType, builder.build());

            Map<AttributeType, Inventory<?>> attributeTypeDefaults = new HashMap<>();
            for (AttributeType attributeType : AttributeType.values()) {
                List<MenuPreset> attributeTypePresets = MenuPreset.getPresetsOfType(MenuPreset.MenuType.DATA_SECOND_LEVEL, attributeType);
                Inventory.Builder attributeBuilder = Interact.inventory().size(attributeTypePresets.size()).name(petType.humanName() + " Attributes: " + StringForm.create(attributeType).getCaptalisedName());

                for (int i = 0; i < attributeTypePresets.size(); i++) {
                    MenuPreset preset = attributeTypePresets.get(i);
                    attributeBuilder.at(Interact.position().slot(i).icon(preset.getIcon()));
                }
                attributeTypeDefaults.put(attributeType, attributeBuilder.build());
            }

            ATTRIBUTE_TYPE_DEFAULTS.put(petType, Collections.unmodifiableMap(attributeTypeDefaults));
        }
    }
    
    public static Inventory<?> getDefault(PetType petType) {
        return TYPE_DEFAULTS.get(petType);
    }

    public static Inventory<?> getDefault(PetType petType, AttributeType attributeType) {
        return ATTRIBUTE_TYPE_DEFAULTS.get(petType).get(attributeType);
    }

    public static Inventory<?> getInventory(final Pet pet) {
        Inventory<?> inventory;
        try {
            inventory = Interact.inventory().from(MenuSettings.ATTRIBUTES_INVENTORY.getValue(pet.getType().storageName())).build();
        } catch (Exception e) {
            inventory = getDefault(pet.getType());
        }
        
        for (Map.Entry<Integer, Icon> entry : inventory.getLayout().getIcons().entrySet()) {
            int slot = entry.getKey();
            Icon icon = entry.getValue();
            Icon.Builder iconBuilder = icon.builder();
            final MenuPreset preset = MenuPreset.getIconToTypeMap().get(icon);
            if (preset == null) {
                continue;
            }

            if (preset.getAttributeType().equals(AttributeType.SWITCH)) {
                iconBuilder = iconBuilder.onPrepare(new PrepareIconAction() {
                    @Override
                    public void onPrepare(Icon icon, PlayerContainer viewer) {
                        boolean active = pet.getActiveAttributes().contains(preset.getAttribute());
                        String on = "[TOGGLE ON]";
                        String off = "[TOGGLE OFF]";
                        switch (ServerUtil.getServerBrand().getCapsule()) {
                            case BUKKIT:
                                icon.setName(active ? ChatColor.GREEN + on : ChatColor.RED + off);
                                break;
                            case SPONGE:
                                // TODO: implement
                                break;
                        }
                    }

                    @Override
                    public String getId() {
                        return "toggle";
                    }
                });
                
            } else if (!(icon instanceof CommandIcon)) {
                iconBuilder.onClick(new AttributeClickAction(pet, preset));
            }
            inventory.getLayout().set(slot, iconBuilder.build());
        }
        
        inventory.getLayout().set(inventory.getLayout().getMaximumSize() - 1, Interact.icon(MenuPreset.CLOSE_DATA.getIcon()).build());
        return inventory;
    }
    
    private static class AttributeClickAction implements ClickAction {

        private Pet pet;
        private MenuPreset preset;

        public AttributeClickAction(Pet pet, MenuPreset preset) {
            this.pet = pet;
            this.preset = preset;
        }

        @Override
        public void onClick(Inventory inventory, PlayerContainer player, Position clicked) {
            if (pet.getOwner().get() != null) {
                if (preset.getAttribute() != null) {
                    if (preset.getAttribute() instanceof Attributes.Attribute) {
                        pet.invertAttribute((Attributes.Attribute) preset.getAttribute());
                        Particle.RED_SMOKE.builder().show(pet.getLocation());
                    } else {
                        pet.setAttribute(preset.getAttribute());
                    }
                } else {
                    inventory.close(player);
                    getSecondLevelInventory(pet, preset.getAttributeType()).show(player);
                }
            }
        }

        @Override
        public String getId() {
            return "invert";
        }

        private static Inventory<?> getSecondLevelInventory(final Pet pet, AttributeType attributeType) {
            Inventory<?> inventory;
            try {
                inventory = Interact.inventory().from(MenuSettings.ATTRIBUTE_SWITCHES_INVENTORY.getValue(attributeType.getConfigName(), pet.getType().storageName())).build();
            } catch (Exception e) {
                inventory = getDefault(pet.getType(), attributeType);
            }

            for (Map.Entry<Integer, Icon> entry : inventory.getLayout().getIcons().entrySet()) {
                int slot = entry.getKey();
                Icon icon = entry.getValue();
                Icon.Builder iconBuilder = icon.builder();
                final MenuPreset preset = MenuPreset.getIconToTypeMap().get(icon);
                if (preset == null) {
                    continue;
                }

                if (!(iconBuilder instanceof CommandIcon)) {
                    iconBuilder.onClick(new AttributeClickAction(pet, preset));
                }

                inventory.getLayout().set(slot, iconBuilder.build());
            }

            inventory.getLayout().set(inventory.getLayout().getMaximumSize() - 1, MenuPreset.BACK.getIcon().builder().onClick(new ClickAction() {
                @Override
                public void onClick(Inventory inventory, PlayerContainer player, Position clicked) {
                    inventory.close(player);
                    getInventory(pet).show(player);
                }

                @Override
                public String getId() {
                    return "back";
                }
            }).build());

            return inventory;
        }
    }
}