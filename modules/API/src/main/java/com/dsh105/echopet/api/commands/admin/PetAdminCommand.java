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

package com.dsh105.echopet.api.commands.admin;

import com.dsh105.echopet.api.commands.influx.EchoPetCommandManager;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.influx.CommandListener;

public class PetAdminCommand implements CommandListener {

    public PetAdminCommand() {
        EchoPetCommandManager manager = EchoPet.getCommandManager();
        manager.register(this);
        manager.nestCommandsIn(this, new ReloadCommand());
    }
}