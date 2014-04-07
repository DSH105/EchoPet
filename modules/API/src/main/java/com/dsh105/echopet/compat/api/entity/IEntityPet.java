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

package com.dsh105.echopet.compat.api.entity;

import com.dsh105.echopet.compat.api.ai.IPetGoalSelector;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface IEntityPet {

    public IPetGoalSelector getPetGoalSelector();

    public SizeCategory getSizeCategory();

    public ICraftPet getBukkitEntity();

    public void resizeBoundingBox(boolean flag);

    public void remove(boolean makeSound);

    public boolean isDead();

    public boolean onInteract(Player p);

    public void setShouldVanish(boolean flag);

    public void setInvisible(boolean flag);

    public void setTarget(LivingEntity livingEntity);

    public LivingEntity getTarget();
}