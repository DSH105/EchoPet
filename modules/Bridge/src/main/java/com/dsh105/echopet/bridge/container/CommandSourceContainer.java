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

package com.dsh105.echopet.bridge.container;

import com.dsh105.commodus.Affirm;
import com.dsh105.commodus.ServerBrand;
import com.dsh105.echopet.bridge.PlayerBridge;
import org.bukkit.command.CommandSender;
import org.spongepowered.api.text.message.Messages;
import org.spongepowered.api.util.command.CommandSource;

public class CommandSourceContainer {

    private Object commandSource;
    private ServerBrand.Capsule serverCapsule;

    protected CommandSourceContainer(Object commandSource, ServerBrand.Capsule serverCapsule) {
        this.commandSource = commandSource;
        this.serverCapsule = serverCapsule;
    }

    public static CommandSourceContainer from(CommandSender commandSender) {
        return new CommandSourceContainer(commandSender, ServerBrand.Capsule.BUKKIT);
    }

    public static CommandSourceContainer from(CommandSource commandSource) {
        return new CommandSourceContainer(commandSource, ServerBrand.Capsule.SPONGE);
    }
    
    public static CommandSourceContainer from(PlayerBridge player) {
        return new CommandSourceContainer(player, ServerBrand.Capsule.UNKNOWN);
    }
    
    public void reset() {
        commandSource = null;
        serverCapsule = null;
    }
    
    public Object get() {
        return commandSource;
        
    }
    
    public CommandSender asBukkit() {
        Affirm.isTrue(serverCapsule == ServerBrand.Capsule.BUKKIT);
        return (CommandSender) get();
    }

    public CommandSource asSponge() {
        Affirm.isTrue(serverCapsule == ServerBrand.Capsule.SPONGE);
        return (CommandSource) get();
    }
    
    public boolean isPermitted(String permission) {
        switch (serverCapsule) {
            case UNKNOWN:
                return ((PlayerBridge) get()).isPermitted(permission);
            default:
                // console, command block, etc.
                return true;
        }
    }
    
    public void sendMessage(String message) {
        switch (serverCapsule) {
            case BUKKIT:
                asBukkit().sendMessage(message);
                break;
            case SPONGE:
                asSponge().sendMessage(Messages.of(message));
                break;
            default:
                ((PlayerBridge) get()).sendMessage(message);
        }
    }
    
    // TODO: fancy messages
}