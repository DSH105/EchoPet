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

package io.github.dsh105.echopet.nms.v1_7_R1.entity;

import com.dsh105.dshutils.util.GeometryUtil;
import io.github.dsh105.echopet.api.entity.nms.IEntityPacketPet;
import io.github.dsh105.echopet.api.entity.pet.Pet;
import io.github.dsh105.echopet.reflection.SafeField;
import io.github.dsh105.echopet.util.ReflectionUtil;
import io.github.dsh105.echopet.util.protocol.wrapper.WrappedDataWatcher;
import io.github.dsh105.echopet.util.protocol.wrapper.WrapperPacketEntityMetadata;
import io.github.dsh105.echopet.util.protocol.wrapper.WrapperPacketNamedEntitySpawn;
import net.minecraft.server.v1_7_R1.World;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class EntityPacketPet extends EntityPet implements IEntityPacketPet {

    protected WrappedDataWatcher dataWatcher;
    protected byte entityStatus = 0;
    protected boolean initiated;
    protected int id;

    public EntityPacketPet(World world) {
        super(world);
    }

    public EntityPacketPet(World world, Pet pet) {
        super(world, pet);
        this.id = new SafeField<Integer>(ReflectionUtil.getNMSClass("Entity"), "id").get(this);
        this.dataWatcher = new WrappedDataWatcher(this);
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

    public void updatePosition() {
        WrapperPacketNamedEntitySpawn spawn = this.getSpawnPacket();
        for (Player p : GeometryUtil.getNearbyPlayers(new Location(this.world.getWorld(), this.locX, this.locY, this.locZ), 50)) {
            spawn.send(p);
        }
    }

    private void updateDatawatcher(String name) {
        dataWatcher.watch(0, (Object) (byte) this.entityStatus);
        dataWatcher.watch(1, (Object) (short) 0);
        dataWatcher.watch(8, (Object) (byte) 0);
        dataWatcher.watch(10, (Object) (String) name);
        WrapperPacketEntityMetadata meta = new WrapperPacketEntityMetadata();
        meta.setEntityId(this.id);
        meta.setMetadata(dataWatcher);

        for (Player p : GeometryUtil.getNearbyPlayers(new Location(this.world.getWorld(), this.locX, this.locY, this.locZ), 50)) {
            meta.send(p);
        }
    }

    public boolean hasInititiated() {
        return this.initiated;
    }

    private void init() {
        this.updateDatawatcher("Human Pet");
        this.updatePosition();
    }
}