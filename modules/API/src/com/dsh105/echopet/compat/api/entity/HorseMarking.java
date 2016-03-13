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

package com.dsh105.echopet.compat.api.entity;

import org.bukkit.entity.Horse;

public enum HorseMarking {
    NONE(Horse.Style.NONE, 0, 1, 2, 3, 4, 5, 6),
    SOCKS(Horse.Style.WHITE, 256, 257, 258, 259, 260, 261, 262),
    WHITE_PATCH(Horse.Style.WHITEFIELD, 512, 513, 514, 515, 516, 517, 518),
    WHITE_SPOTS(Horse.Style.WHITE_DOTS, 768, 769, 770, 771, 772, 773, 774),
    BLACK_SPOTS(Horse.Style.BLACK_DOTS, 1024, 1025, 1026, 1027, 1028, 1029, 1030);

    private HorseVariant[] a = {HorseVariant.WHITE, HorseVariant.CREAMY, HorseVariant.CHESTNUT, HorseVariant.BROWN, HorseVariant.BLACK, HorseVariant.GRAY, HorseVariant.DARKBROWN};
    private Integer[] b;
    private Horse.Style bukkitStyle;

    HorseMarking(Horse.Style bukkitStyle, Integer... i) {
        this.bukkitStyle = bukkitStyle;
        this.b = i;
    }

    public int getId(HorseVariant v) {
        for (int i = 0; i < HorseVariant.values().length; i++) {
            if (a[i] == v) {
                return b[i];
            }
        }
        return -1;
    }

    public Horse.Style getBukkitStyle() {
        return bukkitStyle;
    }

    public static HorseMarking getForBukkitStyle(Horse.Style style) {
        for (HorseMarking v : values()) {
            if (v.getBukkitStyle().equals(style)) {
                return v;
            }
        }
        return null;
    }
}