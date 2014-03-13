package io.github.dsh105.echopet.conversation;

import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.util.Lang;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class NamePrompt extends StringPrompt {

    private Pet pet;
    private boolean admin;

    public NamePrompt(Pet pet, boolean admin) {
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
            conversationContext.getForWhom().sendRawMessage(EchoPetPlugin.getInstance().prefix + Lang.PET_NAME_TOO_LONG.toString());
            return this;
        }
        conversationContext.setSessionData("name", s);
        return new NameSuccessPrompt(this.pet, this.admin);
    }
}