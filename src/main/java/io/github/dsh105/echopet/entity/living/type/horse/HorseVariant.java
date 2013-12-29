package io.github.dsh105.echopet.entity.living.type.horse;

import org.bukkit.entity.Horse;

public enum HorseVariant {

    WHITE(Horse.Color.WHITE),
    CREAMY(Horse.Color.CREAMY),
    CHESTNUT(Horse.Color.CHESTNUT),
    BROWN(Horse.Color.BROWN),
    BLACK(Horse.Color.BLACK),
    GRAY(Horse.Color.GRAY),
    DARKBROWN(Horse.Color.DARK_BROWN);

    private Horse.Color bukkitColour;

    HorseVariant(Horse.Color bukkitColour) {
        this.bukkitColour = bukkitColour;
    }

    public Horse.Color getBukkitColour() {
        return bukkitColour;
    }

    public static HorseVariant getForBukkitColour(Horse.Color colour) {
        for (HorseVariant v : values()) {
            if (v.getBukkitColour().equals(colour)) {
                return v;
            }
        }
        return null;
    }
}
