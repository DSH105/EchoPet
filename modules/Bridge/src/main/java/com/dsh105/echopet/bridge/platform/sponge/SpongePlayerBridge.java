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

package com.dsh105.echopet.bridge.platform.sponge;

import com.dsh105.commodus.Affirm;
import com.dsh105.commodus.container.PositionContainer;
import com.dsh105.echopet.bridge.PlayerBridge;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class SpongePlayerBridge extends PlayerBridge {

    public SpongePlayerBridge(UUID playerUID) {
        super(playerUID);
    }

    @Override
    public String getName() {
        Affirm.notNull(asBukkit());
        return asBukkit().getName();
    }

    @Override
    public boolean isFlying() {
        Affirm.notNull(asBukkit());
        return asBukkit().isFlying();
    }

    @Override
    public void setFlying(boolean flag) {
        Affirm.notNull(asBukkit());
        asBukkit().setFlying(flag);
    }

    @Override
    public PositionContainer getLocation() {
        Affirm.notNull(asBukkit());
        return PositionContainer.from(asBukkit().getLocation());
    }

    @Override
    public double getLocX() {
        return getLocation().getX();
    }

    @Override
    public double getLocY() {
        return getLocation().getY();
    }

    @Override
    public double getLocZ() {
        return getLocation().getZ();
    }

    @Override
    public float getXRotation() {
        return getLocation().getXRotation();
    }

    @Override
    public float getYRotation() {
        return getLocation().getYRotation();
    }

    @Override
    public boolean isOnGround() {
        Affirm.notNull(asBukkit());
        return asBukkit().isOnGround();
    }

    @Override
    public void setPassenger(Object entity) {
        Affirm.notNull(asBukkit());
        Affirm.checkInstanceOf(Entity.class, entity);
        asBukkit().setPassenger((Entity) entity);
    }

    @Override
    public boolean isOnline() {
        Affirm.notNull(asBukkit());
        return asBukkit().isOnline();
    }

    @Override
    public boolean isDead() {
        Affirm.notNull(asBukkit());
        return asBukkit().isDead();
    }
}