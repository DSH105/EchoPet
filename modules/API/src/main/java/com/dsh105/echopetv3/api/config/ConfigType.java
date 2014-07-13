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

package com.dsh105.echopetv3.api.config;

public enum ConfigType {

    GENERAL("config/config.yml",
            "EchoPet By DSH105",
            "---------------------",
            "Configuration for EchoPet 2.x",
            "See the EchoPet Wiki before editing this file",
            "https://github.com/DSH105/EchoPet/wiki/"),

    PETS("config/pets-config.yml"),

    DATA("data/pet-data.yml"),

    MESSAGES("config/messages.yml",
            "EchoPet By DSH105",
            "---------------------",
            "Language Configuration File"),

    MENU("config/menu-config.yml");

    private String path;
    private String[] header;

    ConfigType(String path, String... header) {
        this.path = path;
        this.header = header;
    }

    public String getPath() {
        return path;
    }

    public String[] getHeader() {
        return header;
    }
}