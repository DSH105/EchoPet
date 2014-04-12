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

package com.dsh105.echopet.api.pet.type;

import com.dsh105.echopet.api.pet.Pet;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.IEntityPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityWolfPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IWolfPet;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

import java.util.UUID;

@EntityPetType(petType = PetType.WOLF)
public class WolfPet extends Pet implements IWolfPet {

    DyeColor collar = DyeColor.RED;
    boolean baby = false;
    boolean tamed = false;
    boolean angry = false;

    public WolfPet(Player owner) {
        super(owner);
    }

    public WolfPet(UUID ownerUuid, IEntityPet entityPet) {
        super(ownerUuid, entityPet);
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

    @Override
    public void setCollarColor(DyeColor dc) {
        ((IEntityWolfPet) getEntityPet()).setCollarColor(dc);
        this.collar = dc;
    }

    @Override
    public DyeColor getCollarColor() {
        return this.collar;
    }

    @Override
    public void setTamed(boolean flag) {
        ((IEntityWolfPet) getEntityPet()).setTamed(flag);
        this.tamed = flag;
    }

    @Override
    public boolean isTamed() {
        return this.tamed;
    }

    @Override
    public void setAngry(boolean flag) {
        ((IEntityWolfPet) getEntityPet()).setAngry(flag);
        this.angry = flag;
    }

    @Override
    public boolean isAngry() {
        return this.angry;
    }
}