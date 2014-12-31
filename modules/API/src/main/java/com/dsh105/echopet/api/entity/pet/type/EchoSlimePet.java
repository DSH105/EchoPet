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
import com.dsh105.echopet.api.entity.SizeCategory;
import com.dsh105.echopet.api.entity.attribute.AttributeValue;
import com.dsh105.echopet.api.entity.attribute.Attributes;
import com.dsh105.echopet.api.entity.entitypet.type.EntitySlimePet;
import com.dsh105.echopet.api.plugin.EchoPet;
import com.dsh105.echopet.bridge.MessageBridge;
import com.dsh105.echopet.bridge.entity.type.SlimeEntityBridge;
import com.dsh105.echopet.api.entity.pet.AbstractPetBase;
import com.dsh105.echopet.util.Perm;

import java.util.UUID;

public class EchoSlimePet<T extends SlimeEntityBridge, S extends EntitySlimePet> extends AbstractPetBase<T, S> implements SlimePet<T, S> {

    private int jumpDelay;

    public EchoSlimePet(UUID playerUID) {
        super(playerUID);
        this.jumpDelay = GeneralUtil.random().nextInt(15) + 10;

        String permission = Perm.DATA.replace("<type>", getType().storageName());
        int size = 2;
        for (Attributes.SlimeSize slimeSize : new Attributes.SlimeSize[]{Attributes.SlimeSize.SMALL, Attributes.SlimeSize.LARGE}) {
            AttributeValue attributeValue = slimeSize.getType().getValue(slimeSize);
            if (EchoPet.getBridge(MessageBridge.class).isPermitted(getOwner().get(), permission.replace("<data>", attributeValue.getConfigName()))) {
                size = slimeSize.getSize();
                break;
            }
        }
        this.setSize(Attributes.SlimeSize.getBySize(size));
    }

    @Override
    public void setSize(Attributes.SlimeSize size) {
        int slimeSize = size.getSize();
        getBridgeEntity().setSize(slimeSize);
        getEntity().modifyBoundingBox(width() * slimeSize, height() * slimeSize);
    }

    @Override
    public Attributes.SlimeSize getSize() {
        return Attributes.SlimeSize.getBySize(getBridgeEntity().getSize());
    }

    @Override
    public SizeCategory getSizeCategory() {
        return getSize() == Attributes.SlimeSize.SMALL ? SizeCategory.TINY : (getSize() == Attributes.SlimeSize.LARGE ? SizeCategory.LARGE : super.getSizeCategory());
    }

    @Override
    public void onLive() {
        super.onLive();
        /*if (getModifier().isGrounded() && jumpDelay-- <= 0) {
            jumpDelay = GeneralUtil.random().nextInt(15) + 10;
            getEntity().makeSound(getDeathSound(), getEntity().getSoundVolume(), ((GeneralUtil.random().nextFloat() - GeneralUtil.random().nextFloat()) * 0.2F + 1.0F) / 0.8F);
            getEntity().jump();
        }*/
    }
}