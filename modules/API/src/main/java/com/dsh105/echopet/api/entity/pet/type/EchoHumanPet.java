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

import com.captainbern.minecraft.protocol.PacketType;
import com.captainbern.minecraft.reflection.MinecraftMethods;
import com.captainbern.minecraft.wrapper.WrappedDataWatcher;
import com.captainbern.minecraft.wrapper.WrappedPacket;
import com.dsh105.commodus.GeometryUtil;
import com.dsh105.commodus.UUIDFetcher;
import com.dsh105.echopet.api.configuration.Settings;
import com.dsh105.echopet.api.entity.entitypet.type.EntityHumanPet;
import com.dsh105.echopet.api.entity.pet.EchoEquipablePet;
import com.dsh105.echopet.bridge.entity.type.HumanEntityBridge;
import com.dsh105.echopet.util.WrappedGameProfile;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

// TODO: fix this
public class EchoHumanPet extends EchoEquipablePet<HumanEntityBridge, EntityHumanPet> implements HumanPet {

    private static final byte ENTITY_STATUS_INVISIBLE = 32;
    private static final byte ENTITY_STATUS_SPRINTING = 8;
    private static final byte ENTITY_STATUS_SNEAKING = 2;
    private static final byte ENTITY_STATUS_DEFAULT = 0;

    protected WrappedDataWatcher dataWatcher;
    private byte entityStatus;
    private boolean initiated;
    private int id;
    private UUID profileUuid;
    private WrappedGameProfile gameProfile;

    public EchoHumanPet(UUID playerUID) {
        super(playerUID);

        this.id = hashCode();
        if (Settings.FIX_HUMAN_SKINS.getValue()) {
            try {
                this.profileUuid = UUIDFetcher.getUUIDOf(getName());
            } catch (Exception ignored) {
            }
        }
        if (this.profileUuid == null) {
            this.profileUuid = UUID.randomUUID();
        }
        this.gameProfile = new WrappedGameProfile(this.profileUuid, getName());
    }

    @Override
    public boolean setName(String name, boolean sendFailMessage) {
        name = name.length() > 16 ? name.substring(0, 16) : name;
        boolean success = super.setName(name, sendFailMessage);
        if (success) {
            if (initiated) {
                updatePosition();
            }

            if (gameProfile != null) {
                gameProfile = WrappedGameProfile.getNewProfile(gameProfile, name);
            }
        }
        return success;
    }

    @Override
    public boolean teleport(Location to) {
        boolean success = super.move(to);
        if (initiated) {
            updatePosition();
        }
        return success;
    }

    @Override
    public WrappedDataWatcher getDataWatcher() {
        return dataWatcher;
    }

    @Override
    public byte getEntityStatus() {
        return entityStatus;
    }

    @Override
    public boolean isInitiated() {
        return initiated;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public UUID getProfileUuid() {
        return profileUuid;
    }

    @Override
    public WrappedGameProfile getGameProfile() {
        return gameProfile;
    }

    @Override
    public void onLive() {
        super.onLive();

        if (getModifier().isInvisible()) {
            this.entityStatus = ENTITY_STATUS_INVISIBLE;
        } else if (getModifier().isSneaking()) {
            this.entityStatus = ENTITY_STATUS_SNEAKING;
        } else if (getModifier().isSprinting()) {
            this.entityStatus = ENTITY_STATUS_SPRINTING;
        } else {
            this.entityStatus = ENTITY_STATUS_DEFAULT;
        }

        if (!this.initiated) {
            this.initialiseDataWatcher();
            this.updatePosition();
            this.initiated = true;
        }
        this.updateDataWatcher();
    }

    private void initialiseDataWatcher() {
        this.dataWatcher = new WrappedDataWatcher();
        this.dataWatcher.setObject(0, this.entityStatus);
        this.dataWatcher.setObject(1, 0);
        this.dataWatcher.setObject(8, 0);
        this.dataWatcher.setObject(10, getName());
    }

    @Override
    public void updatePosition() {
        WrappedPacket spawn = new WrappedPacket(PacketType.Play.Server.NAMED_ENTITY_SPAWN);
        spawn.getIntegers().write(0, this.id);
        spawn.getIntegers().write(1, (int) (getLocation().getX() * 32.0D));
        spawn.getIntegers().write(2, (int) (getLocation().getY() * 32.0D));
        spawn.getIntegers().write(3, (int) (getLocation().getZ() * 32.0D));
        spawn.getBytes().write(0, (byte) (getLocation().getYaw() * 256.0F / 360.0F));
        spawn.getBytes().write(1, (byte) (getLocation().getPitch() * 256.0F / 360.0F));
        spawn.getIntegers().write(4, getWeapon().getType().getId());
        spawn.getDataWatchers().write(0, this.dataWatcher);
        for (Player player : GeometryUtil.getNearbyPlayers(getLocation(), -1)) {
            MinecraftMethods.sendPacket(player, spawn.getHandle());
        }
    }

    @Override
    public void updateDataWatcher() {
        this.dataWatcher.setObject(0, this.entityStatus);
        this.dataWatcher.setObject(10, getName());

        WrappedPacket meta = new WrappedPacket(PacketType.Play.Server.ENTITY_METADATA);
        meta.getIntegers().write(0, this.id);
        meta.getWatchableObjectLists().write(0, this.dataWatcher.getWatchableObjects());
        for (Player player : GeometryUtil.getNearbyPlayers(getLocation(), -1)) {
            MinecraftMethods.sendPacket(player, meta.getHandle());
        }
    }
}
