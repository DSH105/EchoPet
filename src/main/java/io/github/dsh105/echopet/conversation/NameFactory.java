package io.github.dsh105.echopet.conversation;

import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.entity.Pet;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationFactory;

public class NameFactory {

    public static void askForName(Conversable whom, Pet pet, boolean admin) {
        new ConversationFactory(EchoPetPlugin.getInstance())
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