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

public enum ConfigType {
    MAIN("config.yml"), PETS_CONFIG("pets-config.yml"), DATA("data.yml"), LANG_CONFIG("lang.yml"), MENU_CONFIG("menu.yml");

    private String path;

    ConfigType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}