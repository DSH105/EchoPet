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

package com.dsh105.echopetv3.api.entity.pet.type;

import com.dsh105.echopetv3.api.entity.PetInfo;
import com.dsh105.echopetv3.api.entity.PetType;
import com.dsh105.echopetv3.api.entity.SizeCategory;
import com.dsh105.echopetv3.api.entity.entitypet.type.EntitySpiderPet;
import com.dsh105.echopetv3.api.entity.pet.PetBase;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;

@PetInfo(type = PetType.SPIDER, width = 1.4F, height = 0.9F)
public class SpiderPetBase<T extends Spider, S extends EntitySpiderPet> extends PetBase<T, S> implements SpiderPet<T, S> {

    public SpiderPetBase(Player owner) {
        super(owner);
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.REGULAR;
    }

    @Override
    public String getIdleSound() {
        return "mob.spider.say";
    }

    @Override
    public String getDeathSound() {
        return "mob.spider.death";
    }

    @Override
    public void makeStepSound() {
        getEntity().makeSound("mob.spider.step", 0.15F, 1.0F);
    }
}