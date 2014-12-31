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

import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.api.plugin.PluginCore;
import com.dsh105.echopet.api.plugin.PluginNucleus;
import com.dsh105.echopet.bridge.container.CommandSourceContainer;
import com.dsh105.influx.Controller;
import com.dsh105.influx.InfluxManager;
import com.dsh105.influx.dispatch.CommandContext;
import com.dsh105.influx.syntax.ConsumedArgumentSet;

public class EchoPetCommandEvent<S extends CommandSourceContainer> extends CommandContext<S> {

    public EchoPetCommandEvent(InfluxManager<S> manager, Controller controller, S sender, ConsumedArgumentSet consumedArgumentSet) {
        super(manager, controller, sender, consumedArgumentSet);
    }

    public PluginNucleus getPluginNucleus() {
        return EchoPet.getNucleus();
    }

    public PluginCore getPlugin() {
        return EchoPet.getCore();
    }
}