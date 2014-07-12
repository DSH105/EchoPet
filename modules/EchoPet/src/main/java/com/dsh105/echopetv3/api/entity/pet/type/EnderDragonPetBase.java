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
import com.dsh105.echopetv3.api.entity.entitypet.type.EntityEnderDragonPet;
import com.dsh105.echopetv3.api.entity.pet.PetBase;
import com.dsh105.echopetv3.api.event.PetRideMoveEvent;
import com.dsh105.echopetv3.api.plugin.EchoPet;
import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@PetInfo(type = PetType.ENDER_DRAGON, width = 16.0F, height = 8.0F)
public class EnderDragonPetBase extends PetBase<EnderDragon, EntityEnderDragonPet> implements EnderDragonPet {

    public EnderDragonPetBase(Player owner) {
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
    public void onRide(float sideMotion, float forwardMotion) {
        Vector motion = new Vector();
        Location position = getLocation().clone();

        // Retrieve motion of passenger
        sideMotion = getEntity().getPassengerSideMotion() * 0.5F;
        forwardMotion = getEntity().getPassengerForwardMotion();

        PetRideMoveEvent moveEvent = new PetRideMoveEvent(this, forwardMotion, sideMotion);
        EchoPet.getCore().getServer().getPluginManager().callEvent(moveEvent);
        if (moveEvent.isCancelled()) {
            return;
        }
        sideMotion = moveEvent.getSidewardMotionSpeed();
        forwardMotion = moveEvent.getForwardMotionSpeed();

        // Apply changes
        if (sideMotion != 0.0F) {
            position.setYaw(getEntity().getPassenger().getLocation().getYaw() + (sideMotion > 0F ? 90 : -90));
            motion.add(position.getDirection().normalize().multiply(-0.5D));
        }

        if (forwardMotion != 0.0F) {
            position.setYaw(getEntity().getPassenger().getLocation().getYaw());
            motion.add(position.getDirection().normalize().multiply(0.5D));
        }

        getEntity().applyPitchAndYawChanges(getEntity().getPassenger().getLocation().getPitch() * 0.5F, getEntity().getPassenger().getLocation().getYaw() - 180);

        if (JUMP_FIELD != null) {
            if (JUMP_FIELD.get(getEntity().getPassenger())) {
                if (getOwner().isFlying()) {
                    getOwner().setFlying(false);
                }

                motion.setY(getEntity().getPassenger().getLocation().getPitch() >= 50 ? -0.4F : 0.5F);
            }
        }

        position.add(motion.multiply(Math.pow(getRideSpeed(), getRideSpeed())));
        getEntity().updatePosition(position.getX(), position.getY(), position.getZ());
    }
}