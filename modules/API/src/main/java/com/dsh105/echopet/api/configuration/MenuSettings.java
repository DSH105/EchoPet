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

package com.dsh105.echopet.api.configuration;

import com.dsh105.commodus.configuration.Config;
import com.dsh105.commodus.configuration.Option;
import com.dsh105.commodus.configuration.OptionSet;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.attribute.AttributeType;
import com.dsh105.echopet.api.inventory.DataMenu;
import com.dsh105.echopet.api.inventory.PetSelector;

import java.util.Map;

public class MenuSettings extends OptionSet {

    public static final Option<Map<String, Object>> SELECTOR_INVENTORY = option("selector", null);
    public static final Option<Map<String, Object>> ATTRIBUTES_INVENTORY = option("attributes.%s", null);
    public static final Option<Map<String, Object>> ATTRIBUTE_SWITCHES_INVENTORY = option("attributeSwitches.%s.%s", null);

    public static final Option<Boolean> SELECTOR_ALLOW_DROP = option("selector.allowDrop", true);
    public static final Option<Boolean> SELECTOR_SHOW_DISABLED_PETS = option("selector.showDisabledPets", true);
    public static final Option<Boolean> SELECTOR_ONJOIN_ENABLE = option("selector.giveOnJoin.enable", false);
    public static final Option<Boolean> SELECTOR_ONJOIN_USE_PERM = option("selector.giveOnJoin.usePerm", false);
    public static final Option<String> SELECTOR_ONJOIN_PERM = optionString("selector.giveOnJoin.perm", "echopet.selector.join");
    public static final Option<Integer> SELECTOR_ONJOIN_SLOT = option("selector.giveOnJoin.slot", 9);
    public static final Option<Boolean> SELECTOR_ONJOIN_CLEAR = option("selector.clearInvOnJoin", false);

    public MenuSettings(Config config) {
        super(config);
    }

    @Override
    public void setDefaults() {
        getConfig().set(SELECTOR_INVENTORY.getPath(), PetSelector.getInventory().serialize());

        for (PetType petType : PetType.values()) {
            getConfig().set(ATTRIBUTES_INVENTORY.getPath(petType.storageName()), DataMenu.getDefault(petType).serialize());
            for (AttributeType attributeType : AttributeType.values()) {
                getConfig().set(ATTRIBUTE_SWITCHES_INVENTORY.getPath(attributeType.getConfigName(), petType.storageName()), DataMenu.getDefault(petType).serialize());
            }
        }

        try {
            for (Option option : getOptions()) {
                setDefault(option);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set configuration defaults", e);
        }
    }
}