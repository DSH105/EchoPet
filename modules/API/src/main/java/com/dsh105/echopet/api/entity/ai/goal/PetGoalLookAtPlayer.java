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
import com.dsh105.echopet.api.entity.ai.PetGoal;
import com.dsh105.echopet.api.entity.ai.PetGoalType;
import org.bukkit.entity.Entity;

public class PetGoalLookAtPlayer extends PetGoal {

    private Entity targetPlayer;
    private float range;
    private int ticksLeft;
    private float chance;

    public PetGoalLookAtPlayer() {
        this(8.0F, 0.1F);
    }

    public PetGoalLookAtPlayer(float range, float chance) {
        this.range = range;
        this.chance = chance;
    }

    @Override
    public PetGoalType getType() {
        return PetGoalType.TWO;
    }

    @Override
    public String getDefaultKey() {
        return "lookatplayer";
    }

    @Override
    public boolean shouldStart() {
        if (GeneralUtil.random().nextFloat() >= chance) {
            return false;
        } else if (getEntity().getPassenger() != null) {
            return false;
        }
        targetPlayer = getEntity().findPlayer(range);
        return targetPlayer != null;
    }

    @Override
    public boolean shouldContinue() {
        return !targetPlayer.isDead() && getEntity().distanceTo(targetPlayer) < (double) (range * range) && ticksLeft > 0;
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
        getEntity().lookAt(targetPlayer.getLocation().getX(), targetPlayer.getLocation().getY() + 0.12D, targetPlayer.getLocation().getZ(), 10.0F, (float) getEntity().getMaxHeadRotation());
        --ticksLeft;
    }
}