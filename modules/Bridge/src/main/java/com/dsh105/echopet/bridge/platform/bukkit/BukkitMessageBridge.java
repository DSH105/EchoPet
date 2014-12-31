package com.dsh105.echopet.bridge.platform.bukkit;

import com.dsh105.commodus.Affirm;
import com.dsh105.echopet.bridge.MessageBridge;
import com.dsh105.echopet.bridge.PlayerBridge;
import com.dsh105.echopet.util.AffirmationException;
import com.dsh105.powermessage.core.PowerMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.permissions.Permissible;

public class BukkitMessageBridge implements MessageBridge {

    @Override
    public String translateChatColours(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public void send(Object conversable, String message) {
        send(conversable, "", message);
    }

    @Override
    public void send(Object conversable, String prefix, String message) {
        if (conversable instanceof PlayerBridge) {
            send((PlayerBridge) conversable, prefix, message);
            return;
        }
        try {
            sendSender(conversable, prefix, message);
        } catch (AffirmationException e) {
            Affirm.checkInstanceOf(Conversable.class, conversable);
            ((Conversable) conversable).sendRawMessage(prefix + translateChatColours(message));
        }
    }

    @Override
    public void send(PlayerBridge player, String message) {
        send(player, "", message);
    }

    @Override
    public void send(PlayerBridge player, String prefix, String message) {
        player.asBukkit().sendMessage(prefix + translateChatColours(message));
    }

    @Override
    public boolean isPermitted(Object permissible, String permission) {
        Affirm.checkInstanceOf(Permissible.class, permissible);
        return ((Permissible) permissible).hasPermission(permission);
    }

    public void sendSender(Object commandSender, String prefix, String message) {
        Affirm.checkInstanceOf(CommandSender.class, commandSender);
        new PowerMessage(prefix + translateChatColours(message)).send((CommandSender) commandSender);
    }
}