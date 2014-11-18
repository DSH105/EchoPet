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

import com.dsh105.echopet.api.entity.*;
import com.dsh105.echopet.api.entity.entitypet.type.EntityCreeperPet;
import com.dsh105.echopet.api.entity.pet.PetBase;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;

@PetInfo(type = PetType.CREEPER, width = 0.6F, height = 1.9F)
public class EchoCreeperPet extends PetBase<Creeper, EntityCreeperPet> implements CreeperPet {

    private boolean ignited;

    public EchoCreeperPet(Player owner) {
        super(owner);
    }

    @AttributeHandler(data = PetData.POWER)
    @Override
    public void setPowered(boolean flag) {
        getBukkitEntity().setPowered(flag);
    }

    @AttributeHandler(data = PetData.POWER, getter = true)
    @Override
    public boolean isPowered() {
        return getBukkitEntity().isPowered();
    }

    @AttributeHandler(data = PetData.IGNITED)
    @Override
    public void setIgnited(boolean flag) {
        this.ignited = flag;
    }

    @AttributeHandler(data = PetData.IGNITED, getter = true)
    @Override
    public boolean isIgnited() {
        return ignited;
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.REGULAR;
    }

    @Override
    public String getIdleSound() {
        return "";
    }

    @Override
    public String getDeathSound() {
        return "mob.creeper.death";
    }
}