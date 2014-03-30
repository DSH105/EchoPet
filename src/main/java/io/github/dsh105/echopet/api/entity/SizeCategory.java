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

package io.github.dsh105.echopet.api.entity;

import io.github.dsh105.echopet.EchoPetPlugin;
import io.github.dsh105.echopet.api.entity.PetType;

public enum SizeCategory {

    TINY(1),
    REGULAR(1),
    LARGE(3),
    GIANT(4),
    OVERSIZE(10);

    private int mod;

    SizeCategory(int modifier) {
        this.mod = modifier;
    }

    public float getStartWalk(PetType petType) {
        return (EchoPetPlugin.getInstance().options.getConfig().getInt("pets." + petType.toString().toLowerCase().replace("_", " ") + ".startFollowDistance", 12)) * this.mod;
    }

    public float getStopWalk(PetType petType) {
        return (EchoPetPlugin.getInstance().options.getConfig().getInt("pets." + petType.toString().toLowerCase().replace("_", " ") + ".stopFollowDistance", 8)) * this.mod;
    }

    public float getTeleport(PetType petType) {
        return (EchoPetPlugin.getInstance().options.getConfig().getInt("pets." + petType.toString().toLowerCase().replace("_", " ") + ".teleportDistance", 50)) * this.mod;
    }
}