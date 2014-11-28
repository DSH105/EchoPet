package com.dsh105.echopet.bridge.platform.bukkit;

import com.dsh105.commodus.Affirm;
import com.dsh105.echopet.bridge.MessageBridge;
import com.dsh105.echopet.util.AffirmationException;
import com.dsh105.powermessage.core.PowerMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;

public class BukkitMessageBridge implements MessageBridge {

    @Override
    public String translateChatColours(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public void send(Object conversable, String prefix, String message) {
        try {
            sendSender(conversable, prefix, message);
        } catch (AffirmationException e) {
            Affirm.checkInstanceOf(Conversable.class, conversable);
            ((Conversable) conversable).sendRawMessage(prefix  + translateChatColours(message));
        }
    }

    public void sendSender(Object commandSender, String prefix, String message) {
        Affirm.checkInstanceOf(CommandSender.class, commandSender);
        new PowerMessage(prefix + translateChatColours(message)).send((CommandSender) commandSender);
    }
}