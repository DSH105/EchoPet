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

import com.dsh105.echopet.bridge.container.CommandSourceContainer;
import com.dsh105.influx.Controller;
import com.dsh105.influx.InfluxManager;
import com.dsh105.influx.dispatch.Dispatcher;
import com.dsh105.influx.dispatch.SpongeCommandDispatcher;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.spongepowered.api.util.command.CommandSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EchoPetDispatcher extends Dispatcher<CommandSourceContainer> implements CommandExecutor, SpongeCommandDispatcher {

    public EchoPetDispatcher(InfluxManager<CommandSourceContainer> manager) {
        super(manager);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<String> arguments = new ArrayList<>();
        Collections.addAll(arguments, args);
        arguments.add(0, command.getLabel());
        return dispatch(CommandSourceContainer.from(sender), arguments.toArray(new String[0]));
    }

    @Override
    public boolean dispatch(CommandSource source, String arguments) {
        return dispatch(CommandSourceContainer.from(source), arguments.split("\\s+"));
    }

    @Override
    public <T extends CommandSourceContainer> boolean preDispatch(T sender, Controller controller, String input) {
        return dispatch(new EchoPetCommandEvent<>(getManager(), controller, sender, consumedArgumentSets.get(input).get(controller)));
    }
}