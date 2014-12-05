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

package com.dsh105.echopet.compat.nms.v1_7_R1.entity.ai;

import com.dsh105.echopet.compat.api.ai.APetGoalFollowOwner;
import com.dsh105.echopet.compat.api.ai.PetGoalType;
import com.dsh105.echopet.compat.api.event.PetMoveEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.nms.v1_7_R1.entity.EntityPet;
import com.dsh105.echopet.compat.nms.v1_7_R1.entity.type.EntityGhastPet;
import net.minecraft.server.v1_7_R1.EntityPlayer;
import net.minecraft.server.v1_7_R1.GenericAttributes;
import net.minecraft.server.v1_7_R1.Navigation;
import net.minecraft.server.v1_7_R1.PathEntity;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;


public class PetGoalFollowOwner extends APetGoalFollowOwner {

    private EntityPet pet;
    private Navigation nav;
    private int timer = 0;
    private double startDistance;
    private double stopDistance;
    private double teleportDistance;
    //private EntityPlayer owner;

    public PetGoalFollowOwner(EntityPet pet, double startDistance, double stopDistance, double teleportDistance) {
        this.pet = pet;
        this.nav = pet.getNavigation();
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        this.teleportDistance = teleportDistance;
        //this.owner = ((CraftPlayer) pet.getPlayerOwner()).getHandle();
    }

    @Override
    public PetGoalType getType() {
        return PetGoalType.THREE;
    }

    @Override
    public String getDefaultKey() {
        return "FollowOwner";
    }

    @Override
    public boolean shouldStart() {
        if (!this.pet.isAlive()) {
            return false;
        } else if (this.pet.getPlayerOwner() == null) {
            return false;
        } else if (this.pet.e(((CraftPlayer) this.pet.getPlayerOwner()).getHandle()) < this.startDistance) {
            return false;
        } else {
            return !(this.pet.getGoalTarget() != null && this.pet.getGoalTarget().isAlive());
        }

    }

    @Override
    public boolean shouldContinue() {
        if (this.nav.g()) {
            return false;
        } else if (this.pet.getPlayerOwner() == null) {
            return false;
        } else if (this.pet.e(((CraftPlayer) this.pet.getPlayerOwner()).getHandle()) <= this.stopDistance) {
            return false;
        }
        PetGoalMeleeAttack attackGoal = (PetGoalMeleeAttack) this.pet.petGoalSelector.getGoal("Attack");
        return !(attackGoal != null && attackGoal.isActive);
    }

    @Override
    public void start() {
        this.timer = 0;

        //Set pathfinding radius
        pet.getAttributeInstance(GenericAttributes.b).setValue(this.teleportDistance);
    }

    @Override
    public void finish() {
        this.nav.h();
    }

    @Override
    public void tick() {
        //https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/PathfinderGoalFollowOwner.java#L57
        EntityPlayer owner = ((CraftPlayer) this.pet.getPlayerOwner()).getHandle();
        this.pet.getControllerLook().a(owner, 10.0F, (float) this.pet.x());
        if (--this.timer <= 0) {
            this.timer = 10;
            if (this.pet.getPlayerOwner().isFlying()) {
                //Don't move pet when owner flying
                return;
            }

            double speed = 0.55F;
            if (this.pet.e(owner) > (this.teleportDistance) && ((CraftPlayer) this.pet.getPlayerOwner()).getHandle().onGround) {
                this.pet.getPet().teleportToOwner();
                return;
            }

            PetMoveEvent moveEvent = new PetMoveEvent(this.pet.getPet(), this.pet.getLocation(), this.pet.getPlayerOwner().getLocation());
            EchoPet.getPlugin().getServer().getPluginManager().callEvent(moveEvent);
            if (moveEvent.isCancelled()) {
                return;
            }

            if (pet.goalTarget == null) {
                if (pet instanceof EntityGhastPet) {
                    PathEntity path = pet.world.a(pet, pet.getPlayerOwner().getLocation().getBlockX(), pet.getPlayerOwner().getLocation().getBlockY() + 5, pet.getPlayerOwner().getLocation().getBlockZ(), (float) pet.getAttributeInstance(GenericAttributes.b).getValue(), true, false, false, true);
                    pet.setPathEntity(path);
                    nav.a(path, speed);
                }
                //Smooth path finding to entity instead of location
                PathEntity path = pet.world.findPath(pet, owner, (float) pet.getAttributeInstance(GenericAttributes.b).getValue(), true, false, false, true);
                pet.setPathEntity(path);
                nav.a(path, speed);
            }
        }
    }
}
