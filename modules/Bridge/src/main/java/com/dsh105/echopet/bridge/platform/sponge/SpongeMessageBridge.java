package com.dsh105.echopet.bridge.platform.sponge;

import com.dsh105.commodus.Affirm;
import com.dsh105.commodus.sponge.SpongeUtil;
import com.dsh105.echopet.bridge.MessageBridge;
import com.dsh105.echopet.bridge.PlayerBridge;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.text.message.Messages;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.source.ConsoleSource;

public class SpongeMessageBridge implements MessageBridge {

    @Override
    public String translateChatColours(String message) {
        // TODO: nope?
        return Messages.replaceCodes(message, '&');
    }

    @Override
    public void send(Object conversable, String message) {
        send(conversable, "", message);
    }

    @Override
    public void send(Object conversable, String prefix, String message) {
        sendSender(conversable, prefix, message);
    }

    @Override
    public void send(PlayerBridge player, String message) {
        send(player, "", message);
    }

    @Override
    public void send(PlayerBridge player, String prefix, String message) {
        player.asSponge().sendMessage(prefix + translateChatColours(message));
    }

    @Override
    public boolean isPermitted(Object permissible, String permission) {
        if (permissible instanceof ConsoleSource) {
            return true;
        }
        Affirm.checkInstanceOf(Player.class, permissible);
        return SpongeUtil.getGame().getServiceManager().provideUnchecked(PermissionService.class).login((Player) permissible).isPermitted(permission);
    }

    public void sendSender(Object commandSource, String prefix, String message) {
        if (commandSource instanceof PlayerBridge) {
            send((PlayerBridge) commandSource, prefix, message);
            return;
        }
        Affirm.checkInstanceOf(CommandSource.class, commandSource);
        ((CommandSource) commandSource).sendMessage(prefix + translateChatColours(message));
    }
}