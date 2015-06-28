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

package com.dsh105.echopet.compat.nms.v1_7_R4.entity;

import com.captainbern.minecraft.protocol.PacketType;
import com.captainbern.minecraft.reflection.MinecraftMethods;
import com.captainbern.minecraft.wrapper.WrappedDataWatcher;
import com.captainbern.minecraft.wrapper.WrappedPacket;
import com.dsh105.commodus.GeometryUtil;
import com.dsh105.commodus.ServerUtil;
import com.dsh105.echopet.compat.api.entity.IEntityPacketPet;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.plugin.uuid.UUIDFetcher;
import com.dsh105.echopet.compat.api.util.wrapper.WrappedGameProfile;
import net.minecraft.server.v1_7_R4.World;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class EntityPacketPet extends EntityPet implements IEntityPacketPet {

    protected WrappedDataWatcher customDataWatcher;
    protected byte entityStatus = 0;
    protected boolean initiated;
    protected int id;
    protected UUID profileUuid;
    protected WrappedGameProfile profile;
    protected int equipmentId = 0;

    public EntityPacketPet(World world) {
        super(world);
    }

    public EntityPacketPet(World world, IPet pet) {
        super(world, pet);
        this.id = this.hashCode();
        if (EchoPet.getConfig().getBoolean("enableHumanSkinFixing", true)) {
            try {
                this.profileUuid = UUIDFetcher.getUUIDOf(pet.getPetName());
            } catch (Exception e) {
            }
        }
        if (this.profileUuid == null) {
            this.profileUuid = UUID.randomUUID();
        }
        this.profile = new WrappedGameProfile(this.profileUuid, pet.getPetName());
        customDataWatcher = new WrappedDataWatcher();
        customDataWatcher.setEntity(this.getBukkitEntity());
    }

    @Override
    public void remove(boolean makeSound) {
        bukkitEntity.remove();
    }

    @Override
    public void onLive() {
        super.onLive();
        if (this.isInvisible()) {
            this.entityStatus = 32;
        } else if (this.isSneaking()) {
            this.entityStatus = 2;
        } else if (this.isSprinting()) {
            this.entityStatus = 8;
        } else {
            this.entityStatus = 0;
        }
        if (!this.initiated) {
            this.init();
            this.initiated = true;
        }
        this.updateDatawatcher(this.pet.getPetName());
    }

    public abstract WrappedPacket getSpawnPacket();

    @Override
    public void updatePosition() {
        WrappedPacket spawnPacket = getSpawnPacket();
        for (Player p : GeometryUtil.getNearbyPlayers(new Location(this.world.getWorld(), this.locX, this.locY, this.locZ), 50)) {
            ServerUtil.sendPacket(spawnPacket.getHandle(), p);
        }
    }

    private void updateDatawatcher(String name) {
        customDataWatcher.setObject(0, (Object) (byte) this.entityStatus);
        customDataWatcher.setObject(1, (Object) (short) 0);
        customDataWatcher.setObject(8, (Object) (byte) 0);
        customDataWatcher.setObject(10, (Object) (String) name);
        WrappedPacket metaPacket = new WrappedPacket(PacketType.Play.Server.ENTITY_METADATA);
        metaPacket.getIntegers().write(0, this.id);
        metaPacket.getWatchableObjectLists().write(0, this.customDataWatcher.getWatchableObjects());

        for (Player p : GeometryUtil.getNearbyPlayers(new Location(this.world.getWorld(), this.locX, this.locY, this.locZ), 50)) {
            ServerUtil.sendPacket(metaPacket.getHandle(), p);
        }
    }

    @Override
    public boolean hasInititiated() {
        return this.initiated;
    }

    private void init() {
        this.updateDatawatcher("Human Pet");
        this.updatePosition();
    }
}
