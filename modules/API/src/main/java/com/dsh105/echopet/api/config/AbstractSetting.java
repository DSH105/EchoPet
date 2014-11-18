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

import com.dsh105.commodus.config.Option;
import com.dsh105.echopet.api.plugin.EchoPet;

public class AbstractSetting<T> extends Option<T> {

    protected ConfigType configType;

    public AbstractSetting(ConfigType configType, String path, String... comments) {
        super(EchoPet.getCore().getConfig(configType).config(), path, comments);
        this.configType = configType;
    }

    public AbstractSetting(ConfigType configType, String path, T defaultValue, String... comments) {
        super(EchoPet.getCore().getConfig(configType).config(), path, defaultValue, comments);
        this.configType = configType;
    }

    public AbstractSetting(String path, T defaultValue, String... comments) {
        this(ConfigType.GENERAL, path, defaultValue, comments);
    }

    public T getValue(Object... replacements) {
        return super.getValue(EchoPet.getCore().getSettings(configType), replacements);
    }

    public void setValue(T value, Object... replacements) {
        super.setValue(EchoPet.getCore().getSettings(configType), value, replacements);
    }
}