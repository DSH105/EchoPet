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

package com.dsh105.echopet.api.commands;

import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.bridge.PlayerBridge;
import com.dsh105.influx.Controller;
import com.dsh105.influx.InfluxManager;
import com.dsh105.influx.dispatch.CommandContext;
import com.dsh105.influx.response.ResponseLevel;
import com.dsh105.influx.syntax.ConsumedArgumentSet;

public class PlayerCommandEvent extends CommandContext {
    
    private PlayerCommandEvent(Controller controller, PlayerBridge sender, ConsumedArgumentSet consumedArgumentSet) {
        super(null, controller, sender, consumedArgumentSet);
    }
    
    public static PlayerCommandEvent of(CommandContext<?> context) {
        if (context.sender() instanceof org.bukkit.entity.Player) {
            return new PlayerCommandEvent(context.getController(), PlayerBridge.of((org.bukkit.entity.Player) context.sender()), context.getConsumedArgumentSet());
        } else if (context.sender() instanceof org.spongepowered.api.entity.player.Player) {
            return new PlayerCommandEvent(context.getController(), PlayerBridge.of((org.spongepowered.api.entity.player.Player) context.sender()), context.getConsumedArgumentSet());
        }
        throw new IllegalArgumentException("Sender is not a player.");
    }

    @Override
    public InfluxManager getManager() {
        return EchoPet.getCommandManager();
    }

    @Override
    public void respond(String response, ResponseLevel level) {
        getManager().respond(sender().get(), response, level);
    }

    @Override
    public void respondAnonymously(String response, ResponseLevel level) {
        getManager().respondAnonymously(sender().get(), response, level);
    }
    
    @Override
    public PlayerBridge sender() {
        return (PlayerBridge) super.sender();
    }
}