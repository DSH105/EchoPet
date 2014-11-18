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

import com.dsh105.echopet.api.entity.ai.PetGoal;
import com.dsh105.echopet.api.entity.ai.PetGoalType;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.api.entity.pet.type.GhastPet;
import org.bukkit.entity.Player;

public class PetGoalFollowOwner extends PetGoal {

    private double speed = 1.65F;

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
    public PetGoalType getType() {
        return PetGoalType.THREE;
    }

    @Override
    public String getDefaultKey() {
        return "followowner";
    }

    @Override
    public boolean shouldStart() {
        if (getBukkitEntity().isDead()) {
            return false;
        } else if (getPet().getOwner() == null) {
            return false;
        } else if (getModifier().distanceTo(getPet().getOwner()) < startDistance) {
            return false;
        } // else return !(getPet.getGoalTarget() != null && !getPet().getGoalTarget().isDead)
        // TODO
        return true;
    }

    @Override
    public boolean shouldContinue() {
        if (getModifier().isNavigating()) {
            return false;
        } else if (getPet().getOwner() == null || !getPet().getOwner().isOnline()) {
            return false;
        } else if (getModifier().distanceTo(getPet().getOwner()) <= stopDistance) {
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
        Player owner = getPet().getOwner();
        getModifier().lookAt(owner, 10.0F, (float) getModifier().getMaxHeadRotation());
        if (--timer <= 0) {
            timer = 10;
            if (owner.isFlying()) {
                return;
            }

            if (getModifier().distanceTo(owner) > teleportDistance && owner.isOnGround()) {
                getPet().teleportToOwner();
            }
        }

        if (getPet() instanceof GhastPet) {
            getModifier().navigateTo(owner.getLocation().getBlockX(), owner.getLocation().getBlockY() + 5, owner.getLocation().getBlockZ(), speed);
        } else {
            getModifier().navigateTo(owner, speed);
        }
    }
}