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

package com.dsh105.echopet.api.entity;

import com.dsh105.echopet.api.configuration.PetSettings;

public enum SizeCategory {

    TINY(1),
    REGULAR(1),
    LARGE(3),
    EXTRA_LARGE(4),
    OVERSIZE(10);

    private int modifier;

    SizeCategory(int modifier) {
        this.modifier = modifier;
    }

    public double startFollowDistance(PetType petType) {
        return (PetSettings.START_FOLLOW_DISTANCE.getValue(petType.storageName()) * this.modifier) / 2D;
    }

    public double stopFollowDistance(PetType petType) {
        return (PetSettings.STOP_FOLLOW_DISTANCE.getValue(petType.storageName()) * this.modifier) / 2D;
    }

    public double teleportDistance(PetType petType) {
        return (PetSettings.TELEPORT_DISTANCE.getValue(petType.storageName()) * this.modifier) / 2D;
    }
}