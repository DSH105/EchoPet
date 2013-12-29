package io.github.dsh105.echopet.entity.living.type.horse;


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
