package com.dsh105.echopet.bridge.platform.sponge.entity;

import com.dsh105.echopet.bridge.entity.RangedEntityBridge;
import org.bukkit.entity.LivingEntity;
import org.spongepowered.api.entity.living.Living;

public class SpongeRangedEntityBridge<E extends Living> extends SpongeLivingEntityBridge<E> implements RangedEntityBridge {

}