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

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.echopet.api.entity.AttributeHandler;
import com.dsh105.echopet.api.entity.PetData;
import com.dsh105.echopet.api.entity.PetInfo;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.entitypet.type.EntityOcelotPet;
import com.dsh105.echopet.api.entity.pet.EchoAgeablePet;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;

@PetInfo(type = PetType.OCELOT, width = 0.6F, height = 0.8F)
public class EchoOcelotPet extends EchoAgeablePet<Ocelot, EntityOcelotPet> implements OcelotPet {

    public EchoOcelotPet(Player owner) {
        super(owner);
    }

    @AttributeHandler(dataType = PetData.Type.CAT_TYPE)
    @Override
    public Ocelot.Type getCatType() {
        return getEntity().getBukkitCatType();
    }

    @AttributeHandler(dataType = PetData.Type.CAT_TYPE, getter = true)
    @Override
    public void setCatType(Ocelot.Type type) {
        getEntity().setBukkitCatType(type);
    }

    @Override
    public String getIdleSound() {
        return (GeneralUtil.random().nextInt(4) == 0 ? "mob.cat.purreow" : "mob.cat.meow");
    }

    @Override
    public String getDeathSound() {
        return "mob.cat.hitt";
    }

    @Override
    public void makeStepSound() {
        getEntity().makeSound("mob.ozelot.step", 0.15F, 1.0F);
    }

    @Override
    public void setStationary(boolean flag) {
        super.setStationary(flag);
        getBukkitEntity().setSitting(flag);
    }
}