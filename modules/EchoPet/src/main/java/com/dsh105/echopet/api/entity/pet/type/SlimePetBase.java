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
import com.dsh105.echopet.api.entity.*;
import com.dsh105.echopet.api.entity.entitypet.type.EntitySlimePet;
import com.dsh105.echopet.api.entity.pet.PetBase;
import com.dsh105.echopet.util.Perm;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

@PetInfo(type = PetType.SLIME, width = 0.6F, height = 0.6F)
public class SlimePetBase<T extends Slime, S extends EntitySlimePet> extends PetBase<T, S> implements SlimePet<T, S> {

    private int jumpDelay;

    public SlimePetBase(Player owner) {
        super(owner);
        this.jumpDelay = GeneralUtil.random().nextInt(15) + 10;

        this.setSize(!getOwner().hasPermission(Perm.DATA.replace("<type>", getType().storageName()).replace("<data>", PetData.MEDIUM.storageName()))
                             ? (!getOwner().hasPermission(Perm.DATA.replace("<type>", getType().storageName()).replace("<data>", PetData.SMALL.storageName())) ? 4 : 1) : 2);
    }

    @AttributeHandler(dataType = PetData.Type.SLIME_SIZE)
    @Override
    public void setSize(int size) {
        getBukkitEntity().setSize(size);
        getEntity().modifyBoundingBox(width() * size, height() * size);
    }

    @AttributeHandler(dataType = PetData.Type.SLIME_SIZE, getter = true)
    @Override
    public int getSize() {
        return getBukkitEntity().getSize();
    }

    @Override
    public SizeCategory getSizeCategory() {
        return getSize() == 1 ? SizeCategory.TINY : (getSize() == 4 ? SizeCategory.LARGE : SizeCategory.REGULAR);
    }

    @Override
    public void onLive() {
        super.onLive();
        if (getEntity().isGrounded() && jumpDelay-- <= 0) {
            jumpDelay = GeneralUtil.random().nextInt(15) + 10;
            getEntity().makeSound(getDeathSound(), getEntity().getSoundVolume(), ((GeneralUtil.random().nextFloat() - GeneralUtil.random().nextFloat()) * 0.2F + 1.0F) / 0.8F);
            getEntity().jump();
        }
    }

    @Override
    public String getIdleSound() {
        return "";
    }

    @Override
    public String getDeathSound() {
        return "mob.slime." + (getSize() > 1 ? "big" : "small");
    }
}