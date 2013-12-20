package io.github.dsh105.echopet.entity.living;

import io.github.dsh105.echopet.entity.ICraftPet;
import io.github.dsh105.echopet.entity.IEntityPet;
import org.bukkit.entity.Creature;

public interface ICraftLivingPet extends ICraftPet, Creature {

    @Override
    public EntityLivingPet getHandle();
}