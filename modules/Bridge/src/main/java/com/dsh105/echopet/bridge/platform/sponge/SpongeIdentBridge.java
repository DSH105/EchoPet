package com.dsh105.echopet.bridge.platform.sponge;

import com.dsh105.commodus.Affirm;
import com.dsh105.commodus.sponge.SpongeIdentUtil;
import com.dsh105.commodus.sponge.SpongeUtil;
import com.dsh105.echopet.bridge.IdentBridge;
import org.spongepowered.api.entity.player.Player;

import java.util.UUID;

public class SpongeIdentBridge implements IdentBridge {

    @Override
    public UUID getUID(Object player) {
        Affirm.checkInstanceOf(Player.class, player);
        return SpongeIdentUtil.getIdent((Player) player);
    }

    @Override
    public Object getPlayer(UUID uniqueId) {
        return SpongeIdentUtil.getPlayer(uniqueId);
    }

    @Override
    public boolean areIdentical(Object player, Object player1) {
        return getUID(player).equals(getUID(player1));
    }

    @Override
    public Object getByName(String targetName, boolean exact) {
        if (exact) {
            return SpongeUtil.getGame().getServer().get().getPlayer(targetName);
        } else {
            // TODO
            return null;
        }
    }

    @Override
    public boolean isPlayer(Object player) {
        return player instanceof Player;
    }
}