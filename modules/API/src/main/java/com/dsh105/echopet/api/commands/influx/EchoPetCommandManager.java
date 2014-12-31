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

package com.dsh105.echopet.api.commands.influx;

import com.dsh105.commodus.ServerUtil;
import com.dsh105.commodus.sponge.SpongeUtil;
import com.dsh105.echopet.api.configuration.Lang;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.bridge.container.CommandSourceContainer;
import com.dsh105.influx.CommandManager;
import com.dsh105.influx.Controller;
import com.dsh105.influx.dispatch.Authorization;
import com.dsh105.influx.help.HelpProvision;
import com.dsh105.influx.registration.bukkit.BukkitRegistry;
import com.dsh105.influx.registration.sponge.SpongeRegistry;
import com.dsh105.influx.response.MessagePurpose;
import org.bukkit.plugin.Plugin;
import org.spongepowered.api.util.command.CommandSource;

public class EchoPetCommandManager extends CommandManager<CommandSourceContainer> {

    public EchoPetCommandManager() {
        super(null, "/", "EchoPet");

        this.dispatcher = new EchoPetDispatcher(this);
        this.setAuthorization(new Authorization<CommandSourceContainer>() {
            @Override
            public boolean authorize(CommandSourceContainer sender, Controller toExecute, String permission) {
                return sender.isPermitted(permission);
            }
        });
        switch (ServerUtil.getServerBrand().getCapsule()) {
            case BUKKIT:
                this.setRegistrationStrategy(new BukkitRegistry(this, (Plugin) EchoPet.getCore(), getDispatcher()));
                this.setHelpProvision(HelpProvision.BUKKIT);
                this.setResponseHandler(new EchoPetBukkitResponder(Lang.PREFIX.getValue()));
            case SPONGE:
                this.setRegistrationStrategy(new SpongeRegistry(this, EchoPet.getCore(), SpongeUtil.getGame(), getDispatcher(), new Authorization<CommandSource>() {
                    @Override
                    public boolean authorize(CommandSource sender, Controller toExecute, String permission) {
                        return getAuthorization().authorize(CommandSourceContainer.from(sender), toExecute, permission);
                    }
                }));
                this.setHelpProvision(HelpProvision.SPONGE);
                this.setResponseHandler(new EchoPetSpongeResponder(Lang.PREFIX.getValue()));
        }
        this.setMessage(MessagePurpose.RESTRICTED_SENDER, "Please log in to perform that command.");
    }

    @Override
    public EchoPetDispatcher getDispatcher() {
        return (EchoPetDispatcher) super.getDispatcher();
    }
}