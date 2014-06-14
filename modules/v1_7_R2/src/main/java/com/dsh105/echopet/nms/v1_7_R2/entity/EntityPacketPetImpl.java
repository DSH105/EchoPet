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

package com.dsh105.echopet.nms.v1_7_R2.entity;

import com.dsh105.commodus.GeometryUtil;
import com.dsh105.echopet.api.entity.nms.EntityPacketPet;
import com.dsh105.echopet.api.entity.pet.Pet;
import com.dsh105.echopet.util.protocol.wrapper.WrappedDataWatcher;
import com.dsh105.echopet.util.protocol.wrapper.WrappedGameProfile;
import com.dsh105.echopet.util.protocol.wrapper.WrapperPacketEntityMetadata;
import com.dsh105.echopet.util.protocol.wrapper.WrapperPacketNamedEntitySpawn;
import net.minecraft.server.v1_7_R2.World;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class EntityPacketPetImpl extends EntityPetImpl implements EntityPacketPet {

    protected WrappedDataWatcher customDataWatcher;
    protected byte entityStatus = 0;
    protected boolean initiated;
    protected int id;
    protected WrappedGameProfile profile;
    protected int equipmentId = 0;

    public EntityPacketPetImpl(World world) {
        super(world);
    }

    public EntityPacketPetImpl(World world, Pet pet) {
        super(world, pet);
        this.id = this.hashCode();
        this.profile = new WrappedGameProfile(String.valueOf(this.id), pet.getPetName());
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

    public abstract WrapperPacketNamedEntitySpawn getSpawnPacket();

    @Override
    public void updatePosition() {
        WrapperPacketNamedEntitySpawn spawn = this.getSpawnPacket();
        for (Player p : GeometryUtil.getNearbyPlayers(new Location(this.world.getWorld(), this.locX, this.locY, this.locZ), 50)) {
            spawn.send(p);
        }
    }

    private void updateDatawatcher(String name) {
        customDataWatcher = new WrappedDataWatcher(this);
        customDataWatcher.initiate(0, (Object) (byte) this.entityStatus);
        customDataWatcher.initiate(1, (Object) (short) 0);
        customDataWatcher.initiate(8, (Object) (byte) 0);
        customDataWatcher.initiate(10, (Object) (String) name);
        WrapperPacketEntityMetadata meta = new WrapperPacketEntityMetadata();
        meta.setEntityId(this.id);
        meta.setMetadata(customDataWatcher);

        for (Player p : GeometryUtil.getNearbyPlayers(new Location(this.world.getWorld(), this.locX, this.locY, this.locZ), 50)) {
            meta.send(p);
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