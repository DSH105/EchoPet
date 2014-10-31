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
import com.dsh105.echopet.api.entity.entitypet.type.EntitySilverfishPet;
import com.dsh105.echopet.api.entity.pet.PetBase;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;

@PetInfo(type = PetType.SILVERFISH, width = 0.3F, height = 0.7F)
public class EchoSilverfishPet extends PetBase<Silverfish, EntitySilverfishPet> implements SilverfishPet {

    public EchoSilverfishPet(Player owner) {
        super(owner);
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.TINY;
    }

    @Override
    public String getIdleSound() {
        return "mob.silverfish.say";
    }

    @Override
    public String getDeathSound() {
        return "mob.silverfish.kill";
    }

    @Override
    public void makeStepSound() {
        getEntity().makeSound("mob.silverfish.step", 0.15F, 1.0F);
    }
}