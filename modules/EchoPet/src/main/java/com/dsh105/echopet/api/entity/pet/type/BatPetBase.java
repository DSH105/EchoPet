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

package com.dsh105.echopet.api.entity.pet.type;

import com.dsh105.echopet.api.entity.PetInfo;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.SizeCategory;
import com.dsh105.echopet.api.entity.entitypet.type.EntityBatPet;
import com.dsh105.echopet.api.entity.pet.PetBase;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@PetInfo(type = PetType.BAT, width = 0.5F, height = 0.9F)
public class BatPetBase extends PetBase<Bat, EntityBatPet> implements BatPet {

    public BatPetBase(Player owner) {
        super(owner);
    }

    @Override
    public void setStartled(boolean flag) {
        getEntity().setStartled(flag);
    }

    @Override
    public boolean isStartled() {
        return getEntity().isStartled();
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.TINY;
    }

    @Override
    public void onLive() {
        super.onLive();
        if (isStartled()) {
            getBukkitEntity().setVelocity(new Vector(0D, 0D, 0D));
            getEntity().setLocY(Math.floor(getEntity().getLocY()) + 1.0D - (double) getEntity().getLength());
        } else {
            getEntity().setMotionY(getEntity().getMotionY() * 0.6000000238418579D);
        }
    }

    @Override
    public String getIdleSound() {
        return "";
    }

    @Override
    public String getDeathSound() {
        return "mob.bat.death";
    }
}