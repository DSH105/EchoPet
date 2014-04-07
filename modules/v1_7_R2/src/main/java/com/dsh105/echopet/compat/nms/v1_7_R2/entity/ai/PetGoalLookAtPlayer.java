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

package com.dsh105.echopet.compat.nms.v1_7_R2.entity.ai;

import com.dsh105.echopet.compat.api.ai.APetGoalLookAtPlayer;
import com.dsh105.echopet.compat.api.ai.PetGoalType;
import com.dsh105.echopet.compat.nms.v1_7_R2.entity.EntityPet;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityHuman;

public class PetGoalLookAtPlayer extends APetGoalLookAtPlayer {

    private EntityPet pet;
    protected Entity player;
    private float range;
    private int ticksLeft;
    private float chance;
    private Class clazz;

    public PetGoalLookAtPlayer(EntityPet pet, Class c) {
        this.pet = pet;
        this.range = 8.0F;
        this.chance = 0.1F;
        this.clazz = c;
    }

    public PetGoalLookAtPlayer(EntityPet pet, Class c, float f, float f1) {
        this.pet = pet;
        this.range = f;
        this.chance = f1;
        this.clazz = c;
    }

    @Override
    public PetGoalType getType() {
        return PetGoalType.TWO;
    }

    @Override
    public String getDefaultKey() {
        return "LookAtPlayer";
    }

    @Override
    public boolean shouldStart() {
        if (this.pet.random().nextFloat() >= this.chance) {
            return false;
        } else if (this.pet.passenger != null) {
            return false;
        } else {
            if (this.clazz == EntityHuman.class) {
                this.player = this.pet.world.findNearbyPlayer(this.pet, (double) this.range);
            } else {
                this.player = this.pet.world.a(this.clazz, this.pet.boundingBox.grow((double) this.range, 3.0D, (double) this.range), this.pet);
            }
            return this.player != null;
        }
    }

    @Override
    public boolean shouldContinue() {
        return this.player.isAlive() && (this.pet.e(this.player) <= (double) (this.range * this.range) && this.ticksLeft > 0);
    }

    @Override
    public void start() {
        this.ticksLeft = 40 + this.pet.random().nextInt(40);
    }

    @Override
    public void finish() {
        this.player = null;
    }

    @Override
    public void tick() {
        this.pet.getControllerLook().a(this.player.locX, this.player.locY + (double) this.player.getHeadHeight(), this.player.locZ, 10.0F, (float) this.pet.bv());
        --this.ticksLeft;
    }
}
