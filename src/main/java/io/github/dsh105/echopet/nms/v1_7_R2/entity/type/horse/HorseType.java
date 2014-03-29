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

package io.github.dsh105.echopet.nms.v1_7_R2.entity.type.horse;


import org.bukkit.entity.Horse;

public enum HorseType {

    NORMAL(Horse.Variant.HORSE, 0),
    DONKEY(Horse.Variant.DONKEY, 1),
    MULE(Horse.Variant.MULE, 2),
    ZOMBIE(Horse.Variant.UNDEAD_HORSE, 3),
    SKELETON(Horse.Variant.SKELETON_HORSE, 4);

    private Horse.Variant bukkitVariant;
    private int id;

    HorseType(Horse.Variant bukkitVariant, int id) {
        this.bukkitVariant = bukkitVariant;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public Horse.Variant getBukkitVariant() {
        return bukkitVariant;
    }

    public static HorseType getForBukkitVariant(Horse.Variant variant) {
        for (HorseType v : values()) {
            if (v.getBukkitVariant().equals(variant)) {
                return v;
            }
        }
        return null;
    }
}
