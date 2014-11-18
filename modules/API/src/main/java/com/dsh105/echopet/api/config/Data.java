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

import com.dsh105.commodus.config.Options;
import com.dsh105.commodus.config.YAMLConfig;

import java.util.ArrayList;

public class Data extends Options {

    public Data(YAMLConfig config) {
        super(config);
    }

    @Override
    public void setDefaults() {

    }

    public static AbstractSetting<String> SECTION = new AbstractSetting<>(ConfigType.DATA, "%s");
    public static AbstractSetting<String> PET_SECTION = new AbstractSetting<>(ConfigType.DATA, "%s.%s");
    public static AbstractSetting<String> PET_TYPE = new AbstractSetting<>(ConfigType.DATA, "%s.%s.pet.type");
    public static AbstractSetting<String> PET_NAME = new AbstractSetting<>(ConfigType.DATA, "%s.%s.pet.name");
    public static Setting<ArrayList<String>> PET_DATA = new Setting<>(ConfigType.DATA, "%s.%s.pet.data");
    public static AbstractSetting<String> RIDER_TYPE = new AbstractSetting<>(ConfigType.DATA, "%s.%s.rider.type");
    public static AbstractSetting<String> RIDER_NAME = new AbstractSetting<>(ConfigType.DATA, "%s.%s.rider.name");
    public static Setting<ArrayList<String>> RIDER_DATA = new Setting<>(ConfigType.DATA, "%s.%s.rider.data");
}