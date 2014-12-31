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

package com.dsh105.echopet.api.entity.ai.goal;

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.echopet.api.entity.ai.Behaviour;
import com.dsh105.echopet.api.entity.ai.BehaviourType;
import com.dsh105.echopet.bridge.Ident;
import com.dsh105.echopet.bridge.PlayerBridge;

public class BehaviourLookAtPlayer extends Behaviour {

    private Object targetPlayer;
    private float range;
    private int ticksLeft;
    private float chance;

    public BehaviourLookAtPlayer() {
        this(8.0F, 0.1F);
    }

    public BehaviourLookAtPlayer(float range, float chance) {
        this.range = range;
        this.chance = chance;
    }

    @Override
    public BehaviourType getType() {
        return BehaviourType.TWO;
    }

    @Override
    public String getDefaultKey() {
        return "lookatplayer";
    }

    @Override
    public boolean shouldStart() {
        if (GeneralUtil.random().nextFloat() >= chance) {
            return false;
        } else if (getModifier().getPassenger() != null) {
            return false;
        }
        targetPlayer = getModifier().findPlayer(range);
        return targetPlayer != null;
    }

    @Override
    public boolean shouldContinue() {
        return !PlayerBridge.of(Ident.get().getUID(targetPlayer)).isDead() && getModifier().distanceTo(targetPlayer) < (double) (range * range) && ticksLeft > 0;
    }

    @Override
    public void start() {
        this.ticksLeft = 40 + GeneralUtil.random().nextInt(40);
    }

    @Override
    public void finish() {
        this.targetPlayer = null;
    }

    @Override
    public void tick() {
        getModifier().lookAt(targetPlayer, 10.0F, (float) getModifier().getMaxHeadRotation());
        --ticksLeft;
    }
}