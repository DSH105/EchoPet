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

import com.captainbern.minecraft.conversion.BukkitUnwrapper;
import com.captainbern.minecraft.reflection.MinecraftReflection;
import com.captainbern.reflection.Reflection;
import com.captainbern.reflection.accessor.FieldAccessor;
import com.captainbern.reflection.matcher.Matchers;
import com.dsh105.commodus.Affirm;
import com.dsh105.commodus.container.PositionContainer;
import com.dsh105.commodus.container.Vector3dContainer;
import com.dsh105.echopet.api.entity.entitypet.type.EntityEnderDragonPet;
import com.dsh105.echopet.bridge.Ident;
import com.dsh105.echopet.bridge.PlayerBridge;
import com.dsh105.echopet.bridge.entity.type.EnderDragonEntityBridge;
import com.dsh105.echopet.api.entity.pet.AbstractPetBase;

import java.util.UUID;

public class EchoEnderDragonPet extends AbstractPetBase<EnderDragonEntityBridge, EntityEnderDragonPet> implements EnderDragonPet {

    public EchoEnderDragonPet(UUID playerUID) {
        super(playerUID);
    }

    @Override
    public void onLive() {
        if (isStationary()) {
            return;
        }

        Object passenger = getModifier().getPassenger();
        if (passenger != null && passenger instanceof org.bukkit.entity.Player && Ident.get().areIdentical(passenger, getOwner().get())) {
            onRide(0F, 0F);
            return;
        }

        getEntity().setHurtTicks(1);
        // Prevent default targeting
        boolean target = getEntity().isTargetingEnabled();
        getEntity().enableTargeting(false);
        // Prevent interaction with EnderCrystals
        getEntity().setTargetCrystal(null);

        getEntity().tickMovement();

        if (target) {
            getEntity().target();
        }
    }

    @Override
    public void onRide(float sideMotion, float forwardMotion) {
        if (getModifier().getPassenger() == null || !getModifier().getPassenger().equals(getOwner().get())) {
            getEntity().updateMotion(sideMotion, forwardMotion);
            getModifier().setStepHeight(0.5F);
            return;
        }

        PositionContainer passengerLocation = getBridgeEntity().getPassengerLocation();

        Vector3dContainer motion = new Vector3dContainer(0, 0, 0);
        PositionContainer position = getLocation().clone();

        // Retrieve motion of passenger
        sideMotion = getModifier().getPassengerSideMotion() * 0.5F;
        forwardMotion = getModifier().getPassengerForwardMotion();

        // Apply changes
        if (sideMotion != 0.0F) {
            position.setXRotation(passengerLocation.getXRotation() + (sideMotion > 0F ? 90 : -90));
            motion.add(position.toVector().normalize().multiply(-0.5D));
        }

        if (forwardMotion != 0.0F) {
            position.setXRotation(passengerLocation.getXRotation());
            motion.add(position.toVector().normalize().multiply(0.5D));
        }

        getModifier().applyPitchAndYawChanges(passengerLocation.getYRotation() * 0.5F, passengerLocation.getXRotation() - 180);

        if (JUMP_FIELD != null) {
            if (JUMP_FIELD.getAccessor().get(getModifier().getPassenger())) {
                if (getOwner().isFlying()) {
                    getOwner().setFlying(false);
                }

                motion.setY(passengerLocation.getYRotation() >= 50 ? -0.4F : 0.5F);
            }
        }

        Vector3dContainer finalMotion = position.toVector().add(motion.multiply(Math.pow(getRideSpeed(), getRideSpeed())));
        getEntity().updatePosition(finalMotion.getX(), finalMotion.getY(), finalMotion.getZ());
    }

    @Override
    public void setTarget(Object entity) {
        Affirm.checkInstanceOf(Entity.class, entity, true);
        // FIXME: not Sponge compatible AT ALL
        // messy :\
        ((FieldAccessor) new Reflection().reflect(MinecraftReflection.getMinecraftClass("EntityEnderDragon")).getSafeFields(Matchers.withExactType(MinecraftReflection.getMinecraftClass("Entity"))).get(0).getAccessor()).set(this, BukkitUnwrapper.getInstance().unwrap(entity));
    }

}