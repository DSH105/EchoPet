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

package com.dsh105.echopet.api.config;

import com.dsh105.commodus.config.Options;
import com.dsh105.commodus.config.YAMLConfig;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.inventory.DataMenu;
import com.dsh105.echopet.api.inventory.PetSelector;
import com.dsh105.echopet.api.plugin.EchoPet;

public class MenuSettings extends Options {

    public MenuSettings(YAMLConfig config) {
        super(config);
    }

    @Override
    public void setDefaults() {
        if (EchoPet.getCore().getConfig().get("petSelector.menu.slots") != null) {
            // Migrate data so it's neater
            PetSelector.getLayout().moveFileDataTo(EchoPet.getCore().getConfig(), "petSelector", getConfig().config(), "selector");
            EchoPet.getCore().getConfig().set("petSelector", null);
        } else {
            PetSelector.getLayout().saveToFile(getConfig().config(), "selector");

            for (PetType petType : PetType.values()) {
                DataMenu.getDefaultLayout(petType).saveToFile(getConfig().config(), "petMenu." + petType.storageName());
            }

            for (AbstractSetting setting : AbstractSetting.getOptions(MenuSettings.class, AbstractSetting.class)) {
                set(setting);
            }
        }
    }

    public static final Setting<Boolean> SELECTOR_ALLOW_DROP = new Setting<>(ConfigType.MENU, "selector.allowDrop", true);
    public static final Setting<Boolean> SELECTOR_SHOW_DISABLED_PETS = new Setting<>(ConfigType.MENU, "selector.showDisabledPets", true);
    public static final Setting<Boolean> SELECTOR_ONJOIN_ENABLE = new Setting<>(ConfigType.MENU, "selector.giveOnJoin.enable", false);
    public static final Setting<Boolean> SELECTOR_ONJOIN_USE_PERM = new Setting<>(ConfigType.MENU, "selector.giveOnJoin.usePerm", false);
    public static final AbstractSetting<String> SELECTOR_ONJOIN_PERM = new AbstractSetting<>(ConfigType.MENU, "selector.giveOnJoin.perm", "echopet.selector.join", new String[0]);
    public static final Setting<Integer> SELECTOR_ONJOIN_SLOT = new Setting<>(ConfigType.MENU, "selector.giveOnJoin.slot", 9);
    public static final Setting<Boolean> SELECTOR_ONJOIN_CLEAR = new Setting<>(ConfigType.MENU, "selector.clearInvOnJoin", false);
}