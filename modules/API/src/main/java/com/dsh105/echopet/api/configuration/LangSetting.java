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
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.bridge.MessageBridge;
import com.dsh105.echopet.bridge.PlayerBridge;

import java.util.List;

public class LangSetting extends Option<String> {

    public LangSetting(String path, String defaultValue, String... comments) {
        super(langConfig(), path, defaultValue, comments);
    }

    public LangSetting(String path, List<String> comments) {
        super(langConfig(), path, comments);
    }

    public LangSetting(String path, String defaultValue, List<String> comments) {
        super(langConfig(), path, defaultValue, comments);
    }

    private static Config langConfig() {
        return EchoPet.getOptions(Lang.class).getConfig();
    }

    @Override
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

    public void send(PlayerBridge player, String... pairedReplacements) {
        EchoPet.getBridge(MessageBridge.class).send(player, Lang.PREFIX.getValue(), "");
    }

    public void send(Object conversable, String... pairedReplacements) {
        String message = getValue(pairedReplacements);
        if (message == null) {
            return;
        }
        EchoPet.getBridge(MessageBridge.class).send(conversable, Lang.PREFIX.getValue(), message);
    }
}