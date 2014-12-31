package com.dsh105.echopet.bridge.platform.sponge.entity;

import com.dsh105.echopet.bridge.entity.AgeableEntityBridge;
import org.spongepowered.api.entity.living.Ageable;

public class SpongeAgeableEntityBridge<E extends Ageable> extends SpongeLivingEntityBridge<E> implements AgeableEntityBridge {

    @Override
    public void setAgeLock(boolean flag) {
        // TODO: hmm
        //getSpongeEntity().setAgeLock(flag);
    }

    @Override
    public boolean isAdult() {
        // TODO: hmm
        //return getSpongeEntity().isAdult();
        return false;
    }

    @Override
    public void setAdult(boolean flag) {
        if (flag) {
            getSpongeEntity().setAdult();
        } else {
            getSpongeEntity().setBaby();
        }
    }
}