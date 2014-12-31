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

import com.dsh105.commodus.reflection.Reflection;
import com.dsh105.echopet.api.entity.entitypet.type.EntityGhastPet;
import com.dsh105.echopet.bridge.entity.type.GhastEntityBridge;
import com.dsh105.echopet.api.entity.pet.AbstractPetBase;

import java.lang.reflect.Field;
import java.util.UUID;

public class EchoGhastPet extends AbstractPetBase<GhastEntityBridge, EntityGhastPet> implements GhastPet {

    private Field aggressionCounter;
    private Field target;

    public EchoGhastPet(UUID playerUID) {
        super(playerUID);

        aggressionCounter = Reflection.getField(Reflection.getNMSClass("EntityGhast"), getEntity().getAggressionCounterFieldName());
        target = Reflection.getField(Reflection.getNMSClass("EntityGhast"), "target");
        nullifyTarget();
    }

    protected void setAggressionCounter(int value) {
        // messy
        try {
            aggressionCounter.set(getEntity(), value);
        } catch (IllegalAccessException e) {
            onError(e);
        }
    }

    protected void nullifyTarget() {
        // messy
        try {
            target.set(getEntity(), null);
        } catch (IllegalAccessException e) {
            onError(e);
        }
    }

    @Override
    public void onLive() {
        // doesn't deviate from the custom goal selector path
        getEntity().setCourseChangeCounter(1);

        // no targeting
        setAggressionCounter(1);
    }
}