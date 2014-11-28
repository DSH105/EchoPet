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
import com.dsh105.commodus.IdentUtil;
import com.dsh105.echopet.api.entity.PetInfo;
import com.dsh105.echopet.api.entity.PetType;
import com.dsh105.echopet.api.entity.SizeCategory;
import com.dsh105.echopet.api.entity.entitypet.type.EntityEnderDragonPet;
import com.dsh105.echopet.api.entity.pet.PetBase;
import com.dsh105.echopet.api.event.bukkit.PetRideMoveEvent;
import com.dsh105.echopet.api.plugin.EchoPet;
import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@PetInfo(type = PetType.ENDER_DRAGON, width = 16.0F, height = 8.0F)
public class EchoEnderDragonPet extends PetBase<EnderDragon, EntityEnderDragonPet> implements EnderDragonPet {

    public EchoEnderDragonPet(Player owner) {
        super(owner);
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.GIANT;
    }

    @Override
    public String getIdleSound() {
        return "mob.enderdragon.growl";
    }

    @Override
    public String getDeathSound() {
        return "";
    }

    @Override
    public void onLive() {
        if (isStationary()) {
            return;
        }

        org.bukkit.entity.Entity passenger = getModifier().getPassenger();
        if (passenger != null && passenger instanceof org.bukkit.entity.Player && IdentUtil.areIdentical((org.bukkit.entity.Player) passenger, getOwner())) {
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
        if (getModifier().getPassenger() == null || getModifier().getPassenger() != getOwner()) {
            getEntity().updateMotion(sideMotion, forwardMotion);
            getModifier().setStepHeight(0.5F);
            return;
        }

        Vector motion = new Vector();
        Location position = getLocation().clone();

        // Retrieve motion of passenger
        sideMotion = getModifier().getPassengerSideMotion() * 0.5F;
        forwardMotion = getModifier().getPassengerForwardMotion();

        PetRideMoveEvent moveEvent = new PetRideMoveEvent(this, forwardMotion, sideMotion);
        EchoPet.getCore().getServer().getPluginManager().callEvent(moveEvent);
        if (moveEvent.isCancelled()) {
            return;
        }
        sideMotion = moveEvent.getSidewardMotionSpeed();
        forwardMotion = moveEvent.getForwardMotionSpeed();

        // Apply changes
        if (sideMotion != 0.0F) {
            position.setYaw(getModifier().getPassenger().getLocation().getYaw() + (sideMotion > 0F ? 90 : -90));
            motion.add(position.getDirection().normalize().multiply(-0.5D));
        }

        if (forwardMotion != 0.0F) {
            position.setYaw(getModifier().getPassenger().getLocation().getYaw());
            motion.add(position.getDirection().normalize().multiply(0.5D));
        }

        getModifier().applyPitchAndYawChanges(getModifier().getPassenger().getLocation().getPitch() * 0.5F, getModifier().getPassenger().getLocation().getYaw() - 180);

        if (JUMP_FIELD != null) {
            if (JUMP_FIELD.getAccessor().get(getModifier().getPassenger())) {
                if (getOwner().isFlying()) {
                    getOwner().setFlying(false);
                }

                motion.setY(getModifier().getPassenger().getLocation().getPitch() >= 50 ? -0.4F : 0.5F);
            }
        }

        position.add(motion.multiply(Math.pow(getRideSpeed(), getRideSpeed())));
        getEntity().updatePosition(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public void setTarget(Entity entity) {
        // messy :\
        ((FieldAccessor) new Reflection().reflect(MinecraftReflection.getMinecraftClass("EntityEnderDragon")).getSafeFields(Matchers.withExactType(MinecraftReflection.getMinecraftClass("Entity"))).get(0).getAccessor()).set(this, BukkitUnwrapper.getInstance().unwrap(entity));
    }

}