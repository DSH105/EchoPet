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

package io.github.dsh105.echopet.conversation;

import io.github.dsh105.echopet.compat.api.entity.IPet;
import io.github.dsh105.echopet.compat.api.plugin.EchoPet;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationFactory;

public class NameFactory {

    public static void askForName(Conversable whom, IPet pet, boolean admin) {
        new ConversationFactory(EchoPet.getPlugin())
                .withModality(true)
                .withLocalEcho(false)
                .withPrefix(new NameConversationPrefix())
                .withTimeout(90)
                .withFirstPrompt(new NamePrompt(pet, admin))
                .withEscapeSequence("exit")
                .withEscapeSequence("quit")
                .buildConversation(whom).begin();
    }
}