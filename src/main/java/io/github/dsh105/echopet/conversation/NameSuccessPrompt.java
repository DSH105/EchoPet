package io.github.dsh105.echopet.conversation;

import com.dsh105.dshutils.util.StringUtil;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.util.Lang;
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
                    .replace("%type%", StringUtil.capitalise(this.pet.getPetType().toString().replace("_", " ")))
                    .replace("%name%", name)

                    : Lang.NAME_PET.toString()
                    .replace("%type%", StringUtil.capitalise(this.pet.getPetType().toString().replace("_", " ")))
                    .replace("%name%", name);
        } else {
            return Lang.NAME_NOT_ALLOWED.toString().replace("%name%", name);
        }
    }
}