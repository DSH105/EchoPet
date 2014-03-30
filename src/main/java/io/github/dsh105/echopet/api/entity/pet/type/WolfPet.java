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

package io.github.dsh105.echopet.api.entity.pet.type;

import io.github.dsh105.echopet.api.entity.EntityPetType;
import io.github.dsh105.echopet.api.entity.IAgeablePet;
import io.github.dsh105.echopet.api.entity.PetType;
import io.github.dsh105.echopet.api.entity.nms.IEntityPet;
import io.github.dsh105.echopet.api.entity.nms.type.IEntityWolfPet;
import io.github.dsh105.echopet.api.entity.pet.Pet;
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

    public WolfPet(String owner, IEntityPet entityPet) {
        super(owner, entityPet);
    }

    public void setCollarColor(DyeColor dc) {
        ((IEntityWolfPet) getEntityPet()).setCollarColor(dc);
        this.collar = dc;
    }

    public DyeColor getCollarColor() {
        return this.collar;
    }

    @Override
    public void setBaby(boolean flag) {
        ((IEntityWolfPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    @Override
    public boolean isBaby() {
        return this.baby;
    }

    public void setTamed(boolean flag) {
        ((IEntityWolfPet) getEntityPet()).setTamed(flag);
        this.tamed = flag;
    }

    public boolean isTamed() {
        return this.tamed;
    }

    public void setAngry(boolean flag) {
        ((IEntityWolfPet) getEntityPet()).setAngry(flag);
        this.angry = flag;
    }

    public boolean isAngry() {
        return this.angry;
    }
}