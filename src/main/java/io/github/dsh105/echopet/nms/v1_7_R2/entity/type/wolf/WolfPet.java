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

package io.github.dsh105.echopet.nms.v1_7_R2.entity.type.wolf;

import io.github.dsh105.echopet.nms.v1_7_R2.entity.*;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.WOLF)
public class WolfPet extends Pet implements IAgeablePet {

    DyeColor collar = DyeColor.RED;
    boolean baby = false;
    boolean tamed = false;
    boolean angry = false;

    public WolfPet(Player owner) {
        super(owner);
    }

    public WolfPet(String owner, EntityPet entityPet) {
        super(owner, entityPet);
    }

    public void setCollarColor(DyeColor dc) {
        ((EntityWolfPet) getEntityPet()).setCollarColor(dc);
        this.collar = dc;
    }

    public DyeColor getCollarColor() {
        return this.collar;
    }

    @Override
    public void setBaby(boolean flag) {
        ((EntityWolfPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    @Override
    public boolean isBaby() {
        return this.baby;
    }

    public void setTamed(boolean flag) {
        ((EntityWolfPet) getEntityPet()).setTamed(flag);
        this.tamed = flag;
    }

    public boolean isTamed() {
        return this.tamed;
    }

    public void setAngry(boolean flag) {
        ((EntityWolfPet) getEntityPet()).setAngry(flag);
        this.angry = flag;
    }

    public boolean isAngry() {
        return this.angry;
    }
}