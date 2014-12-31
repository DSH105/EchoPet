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

import com.dsh105.echopet.api.entity.ai.Behaviour;
import com.dsh105.echopet.api.entity.ai.BehaviourType;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.entity.pet.type.GhastPet;
import com.dsh105.echopet.bridge.PlayerBridge;

public class BehaviourFollowOwner extends Behaviour {

    private double speed = 1.45F;

    private int timer = 0;
    private double startDistance;
    private double stopDistance;
    private double teleportDistance;

    @Override
    protected void setPet(Pet pet) {
        super.setPet(pet);
        startDistance = getPet().getSizeCategory().startFollowDistance(getPet().getType());
        stopDistance = getPet().getSizeCategory().stopFollowDistance(getPet().getType());
        teleportDistance = getPet().getSizeCategory().teleportDistance(getPet().getType());
    }

    @Override
    public BehaviourType getType() {
        return BehaviourType.THREE;
    }

    @Override
    public String getDefaultKey() {
        return "followowner";
    }

    @Override
    public boolean shouldStart() {
        if (getBukkitEntity().isDead()) {
            return false;
        } else if (getPet().getOwner().get() == null) {
            return false;
        } else if (getPet().isOwnerRiding() || getPet().isHat()) {
            return false;
        } else if (getModifier().distanceTo(getPet().getOwner().get()) < startDistance) {
            return false;
        } // else return !(getPet.getGoalTarget() != null && !getPet().getGoalTarget().isDead)
        // TODO
        return true;
    }

    @Override
    public boolean shouldContinue() {
        if (getModifier().isNavigating()) {
            return false;
        } else if (getPet().getOwner().get() == null || !getPet().getOwner().isOnline()) {
            return false;
        } else if (getPet().isOwnerRiding() || getPet().isHat()) {
            return false;
        } else if (getModifier().distanceTo(getPet().getOwner().get()) <= stopDistance) {
            return false;
        } else if (getPet().isStationary()) {
            return false;
        }
        // TODO: Is attack goal active
        return true;
    }

    @Override
    public void start() {
        this.timer = 0;
        getModifier().setPathfindingRadius((float) teleportDistance);
    }

    @Override
    public void finish() {
        getModifier().stopNavigating();
    }

    @Override
    public void tick() {
        PlayerBridge player = getPet().getOwner();
        getModifier().lookAt(player.get(), 10.0F, (float) getModifier().getMaxHeadRotation());
        if (--timer <= 0) {
            timer = 10;
            if (player.isFlying()) {
                return;
            }

            if (getModifier().distanceTo(player.get()) > teleportDistance && player.isOnGround()) {
                getPet().moveToOwner();
            }
        }

        if (getPet() instanceof GhastPet) {
            getModifier().navigateTo((int) player.getLocX(), (int) player.getLocY() + 5, (int) player.getLocZ(), speed);
        } else {
            getModifier().navigateTo(player.get(), speed);
        }
    }
}