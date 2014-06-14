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

import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.util.Lang;
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
        boolean success = this.pet.setPetName(name, false);
        if (success) {
            return this.admin ? Lang.ADMIN_NAME_PET.toString()
                    .replace("%player%", this.pet.getNameOfOwner())
                    .replace("%type%", pet.getPetType().humanName())
                    .replace("%name%", name)

                    : Lang.NAME_PET.toString()
                    .replace("%type%", pet.getPetType().humanName())
                    .replace("%name%", name);
        } else {
            return Lang.NAME_NOT_ALLOWED.toString().replace("%name%", name);
        }
    }
}