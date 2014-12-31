package com.dsh105.echopet.bridge.platform.bukkit;

import com.dsh105.commodus.Affirm;
import com.dsh105.commodus.StringUtil;
import com.dsh105.commodus.bukkit.BukkitIdentUtil;
import com.dsh105.echopet.bridge.IdentBridge;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitIdentBridge implements IdentBridge {

    @Override
    public UUID getUID(Object player) {
        Affirm.checkInstanceOf(Player.class, player);
        return StringUtil.convertUUID(BukkitIdentUtil.getIdent((Player) player));
    }

    @Override
    public Object getPlayer(UUID uniqueId) {
        return BukkitIdentUtil.getPlayer(uniqueId);
    }

    @Override
    public boolean areIdentical(Object player, Object player1) {
        return getUID(player).equals(getUID(player1));
    }

    @Override
    public Object getByName(String targetName, boolean exact) {
        if (exact) {
            return Bukkit.getPlayerExact(targetName);
        } else {
            return Bukkit.getPlayer(targetName);
        }
    }

    @Override
    public boolean isPlayer(Object player) {
        return player instanceof Player;
    }
}