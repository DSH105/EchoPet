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

package com.dsh105.echopetv3.conversation;

import com.dsh105.echopetv3.api.config.Lang;
import com.dsh105.echopetv3.api.entity.pet.Pet;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;

public class NameSuccessPrompt extends MessagePrompt {

    private Pet pet;
    private boolean admin;

    public NameSuccessPrompt(Pet pet, boolean admin) {
        this.pet = pet;
        this.admin = admin;
    }

    @Override
    protected Prompt getNextPrompt(ConversationContext context) {
        return Prompt.END_OF_CONVERSATION;
    }

    @Override
    public String getPromptText(ConversationContext context) {
        String name = (String) context.getSessionData("name");
        if (pet.setName(name, false)) {
            return (this.admin ? Lang.ADMIN_NAME_PET : Lang.NAME_PET).getValue("player", pet.getOwnerName(), "name", pet.getType().humanName(), "newname", pet.getName());
        }
        return Lang.NAME_NOT_ALLOWED.getValue("name", name);
    }
}