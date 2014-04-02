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

package io.github.dsh105.echopet.compat.api.entity;

import io.github.dsh105.echopet.compat.api.plugin.EchoPet;

public enum SizeCategory {

    TINY(1),
    REGULAR(1),
    LARGE(3),
    GIANT(4),
    OVERSIZE(10);

    private int modifier;

    SizeCategory(int modifier) {
        this.modifier = modifier;
    }

    public float getStartWalk(PetType petType) {
        return (EchoPet.getConfig().getInt("pets." + petType.toString().toLowerCase().replace("_", " ") + ".startFollowDistance", 12) * this.modifier) / 2;
    }

    public float getStopWalk(PetType petType) {
        return (EchoPet.getConfig().getInt("pets." + petType.toString().toLowerCase().replace("_", " ") + ".stopFollowDistance", 4) * this.modifier) / 2;
    }

    public float getTeleport(PetType petType) {
        return (EchoPet.getConfig().getInt("pets." + petType.toString().toLowerCase().replace("_", " ") + ".teleportDistance", 30) * this.modifier) / 2;
    }
}