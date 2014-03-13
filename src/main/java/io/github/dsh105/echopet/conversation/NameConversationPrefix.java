package io.github.dsh105.echopet.conversation;

import io.github.dsh105.echopet.EchoPetPlugin;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;

public class NameConversationPrefix implements ConversationPrefix {

    @Override
    public String getPrefix(ConversationContext conversationContext) {
        return EchoPetPlugin.getInstance().prefix;
    }
}