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

package com.dsh105.echopet.conversation;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.Lang;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class NamePrompt extends StringPrompt {

    private IPet pet;
    private boolean admin;

    public NamePrompt(IPet pet, boolean admin) {
        this.pet = pet;
        this.admin = admin;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return this.admin ? Lang.ADMIN_NAME_PET_PROMPT.toString().replace("%player%", pet.getNameOfOwner())
                : Lang.NAME_PET_PROMPT.toString();
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        if (s.length() > 32) {
            conversationContext.getForWhom().sendRawMessage(EchoPet.getPrefix() + Lang.PET_NAME_TOO_LONG.toString());
            return this;
        }
        conversationContext.setSessionData("name", s);
        return new NameSuccessPrompt(this.pet, this.admin);
    }
}