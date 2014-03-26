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

package io.github.dsh105.echopet.util.fanciful;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.com.google.gson.stream.JsonWriter;

final class MessagePart {

    final String text;
    ChatColor color = null;
    ChatColor[] styles = null;
    String clickActionName = null, clickActionData = null,
            hoverActionName = null, hoverActionData = null;

    MessagePart(final String text) {
        this.text = text;
    }

    JsonWriter writeJson(JsonWriter json) {
        try {
            json.beginObject().name("text").value(text);
            if (color != null) {
                json.name("color").value(color.name().toLowerCase());
            }
            if (styles != null) {
                for (final ChatColor style : styles) {
                    json.name(style.name().toLowerCase()).value(true);
                }
            }
            if (clickActionName != null && clickActionData != null) {
                json.name("clickEvent")
                        .beginObject()
                        .name("action").value(clickActionName)
                        .name("value").value(clickActionData)
                        .endObject();
            }
            if (hoverActionName != null && hoverActionData != null) {
                json.name("hoverEvent")
                        .beginObject()
                        .name("action").value(hoverActionName)
                        .name("value").value(hoverActionData)
                        .endObject();
            }
            return json.endObject();
        } catch (Exception e) {
            e.printStackTrace();
            return json;
        }
    }

}