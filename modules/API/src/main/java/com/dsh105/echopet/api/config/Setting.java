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

package com.dsh105.echopet.api.config;

import com.dsh105.echopet.api.plugin.EchoPet;

public class Setting<T> extends AbstractSetting<T> {

    public Setting(ConfigType configType, String path, String... comments) {
        super(configType, path, comments);
    }

    public Setting(ConfigType configType, String path, T defaultValue, String... comments) {
        super(configType, path, defaultValue, comments);
    }

    public Setting(String path, T defaultValue, String... comments) {
        super(path, defaultValue, comments);
    }

    public T getValue(T defaultValue, Object... replacements) {
        return super.getValue(EchoPet.getCore().getSettings(this.configType), defaultValue, replacements);
    }
}