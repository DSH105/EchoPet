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

package com.dsh105.echopet.api.configuration;

import com.dsh105.commodus.configuration.Config;
import com.dsh105.commodus.configuration.Option;
import com.dsh105.commodus.configuration.OptionSet;

import java.util.ArrayList;

public class Data extends OptionSet {

    public static Option<String> SECTION = optionString("%s");
    public static Option<String> PET_SECTION = optionString("%s.%s");
    public static Option<String> PET_TYPE = optionString("%s.%s.pet.type");
    public static Option<String> PET_NAME = optionString("%s.%s.pet.name");
    public static Option<ArrayList<String>> PET_ATTRIBUTES = option("%s.%s.pet.attributes", new ArrayList<String>());
    public static Option<ArrayList<String>> PET_ATTRIBUTES_BY_TYPE = option("%s.%s.pet.attributes.%s", new ArrayList<String>());
    public static Option<String> RIDER_TYPE = optionString("%s.%s.rider.type");
    public static Option<String> RIDER_NAME = optionString("%s.%s.rider.name");
    public static Option<ArrayList<String>> RIDER_ATTRIBUTES = option("%s.%s.rider.attributes", new ArrayList<String>());
    public static Option<ArrayList<String>> RIDER_ATTRIBUTES_BY_TYPE = option("%s.%s.rider.attributes.%s", new ArrayList<String>());

    public Data(Config config) {
        super(config);
    }

    @Override
    public void setDefaults() {

    }
}