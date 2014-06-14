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

package com.dsh105.echopet.compat.api.config;

import com.dsh105.commodus.config.Option;
import com.dsh105.commodus.config.Options;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

public class Setting<T> extends Option<T> {

    private ConfigType configType;

    public Setting(ConfigType configType, String path, Class<T> returnType, String... comments) {
        super(configType.getPath(), path, returnType, comments);
        this.configType = configType;
    }

    public Setting(ConfigType configType, String path, T defaultValue, String... comments) {
        super(configType.getPath(), path, defaultValue, comments);
        this.configType = configType;
    }

    public Setting(String path, T defaultValue, String... comments) {
        this(ConfigType.MAIN, path, defaultValue, comments);
    }

    public T getValue(Object... replacements) {
        return super.getValue(EchoPet.getSettings(configType), replacements);
    }

    public T getValue(T defaultValue, Object... replacements) {
        return super.getValue(EchoPet.getSettings(configType), defaultValue, replacements);
    }

    public void setValue(T value, Object... replacements) {
        super.setValue(EchoPet.getSettings(configType), value, replacements);
    }

    public static ArrayList<Setting> getOptions(ConfigType configType) {
        return getOptions(Settings.class, Setting.class, configType.getPath());
    }
}