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

/*
 * This file is part of HoloAPI.
 *
 * HoloAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HoloAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HoloAPI.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.api.config;

import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.bridge.MessageBridge;

public class LangSetting extends AbstractSetting<String> {

    public LangSetting(ConfigType configType, String path, String defaultValue, String... comments) {
        super(configType, path, defaultValue, comments);
    }

    public LangSetting(String path, String defaultValue, String... comments) {
        super(path, defaultValue, comments);
    }

    public String getValue(String... pairedReplacements) {
        String message = super.getValue();
        for (int i = 0; i < pairedReplacements.length; i += 2) {
            if ((i + 1) >= pairedReplacements.length) {
                break;
            }
            message = message.replace("%" + pairedReplacements[i] + "%", pairedReplacements[i + 1]);
        }

        if (message == null || message.isEmpty() || message.equalsIgnoreCase("NONE")) {
            return null;
        }
        return EchoPet.getBridge(MessageBridge.class).translateChatColours(EchoPet.getCommandManager().getResponder().format("{c1}" + message));
    }

    public void send(Object conversable, String... pairedReplacements) {
        String message = getValue(pairedReplacements);
        if (message == null) {
            return;
        }
        EchoPet.getBridge(MessageBridge.class).send(conversable, Lang.PREFIX.getValue(), message);
    }
}