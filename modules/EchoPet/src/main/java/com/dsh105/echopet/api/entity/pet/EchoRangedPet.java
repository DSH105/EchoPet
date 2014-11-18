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

package com.dsh105.echopet.api.entity.pet;

import com.dsh105.echopet.api.entity.entitypet.EntityRangedPet;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public abstract class EchoRangedPet<T extends LivingEntity, S extends EntityRangedPet> extends PetBase<T, S> implements RangedPet<T, S> {

    protected EchoRangedPet(Player owner) {
        super(owner);
    }

    @Override
    public void rangedAttack(LivingEntity entity, float speed) {
        getEntity().rangedAttack(entity, speed);
    }
}