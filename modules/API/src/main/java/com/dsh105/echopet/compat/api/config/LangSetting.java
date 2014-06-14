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

package com.dsh105.echopet.compat.api.config;

import com.dsh105.commodus.config.Option;
import com.dsh105.commodus.config.Options;
import com.dsh105.echopet.compat.api.event.EchoPetSendMessageEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.conversations.Conversable;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class LangSetting extends Setting<String> {

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
            message = message.replace(pairedReplacements[i], pairedReplacements[i + 1]);
        }

        if (message == null || message.isEmpty() || message.equalsIgnoreCase("NONE")) {
            return null;
        }
        return message;
    }

    public void send(Player player, String... pairedReplacements) {
        send((CommandSender) player, pairedReplacements);
    }

    // Not ideal, but it's easy to call
    public void send(CommandSender sender, String... pairedReplacements) {
        String message = getValue(pairedReplacements);
        if (message == null) {
            return;
        }
        EchoPetSendMessageEvent messageEvent = new EchoPetSendMessageEvent(ChatColor.translateAlternateColorCodes('&', message), this, sender);
        Bukkit.getServer().getPluginManager().callEvent(messageEvent);
        if (!messageEvent.isCancelled()) {
            messageEvent.getRecipient().sendMessage(Lang.PREFIX.getValue() + messageEvent.getMessageToSend());
        }
    }

    public void send(Conversable conversable, String... pairedReplacements) {
        if (conversable instanceof CommandSender) {
            send((CommandSender) conversable, pairedReplacements);
            return;
        }

        String message = getValue(pairedReplacements);
        if (message != null) {
            conversable.sendRawMessage(Lang.PREFIX.getValue() + message);
        }
    }
}