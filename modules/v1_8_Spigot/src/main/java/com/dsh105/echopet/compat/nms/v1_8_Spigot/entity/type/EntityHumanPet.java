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

package com.dsh105.echopet.compat.nms.v1_8_Spigot.entity.type;

import com.captainbern.minecraft.wrapper.WrappedPacket;
import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityHumanPet;
import com.dsh105.echopet.compat.api.util.wrapper.WrappedGameProfile;
import com.dsh105.echopet.compat.nms.v1_8_Spigot.entity.EntityPacketPet;
import net.minecraft.server.v1_7_R4.World;

@EntitySize(width = 0.6F, height = 1.8F)
@EntityPetType(petType = PetType.HUMAN)
public class EntityHumanPet extends EntityPacketPet implements IEntityHumanPet {

    public EntityHumanPet(World world) {
        super(world);
    }

    public EntityHumanPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    public WrappedPacket getSpawnPacket() {
        WrappedPacket spawnPacket = new WrappedPacket(PacketType.Play.Server.NAMED_ENTITY_SPAWN);
        spawnPacket.getIntegers().write(0, this.id);
        spawnPacket.getAccessor().write(1, this.profile);
        spawnPacket.getIntegers().write(1, (int) (this.locX * 32.0D));
        spawnPacket.getIntegers().write(2, (int) (this.locY * 32.0D));
        spawnPacket.getIntegers().write(3, (int) (this.locZ * 32.0D));
        spawnPacket.getBytes().write(0, (byte) (this.yaw * 256.0F / 360.0F));
        spawnPacket.getBytes().write(1, (byte) (this.pitch * 256.0F / 360.0F));
        spawnPacket.getIntegers().write(4, this.equipmentId);
        spawnPacket.getDataWatchers().write(0, this.customDataWatcher);
        return spawnPacket;
    }

    @Override
    public void setEquipmentId(int id) {
        this.equipmentId = id;
    }

    @Override
    public int getEquipmentId() {
        return equipmentId;
    }

    @Override
    public WrappedGameProfile getGameProfile() {
        return profile;
    }

    @Override
    public void setGameProfile(WrappedGameProfile profile) {
        this.profile = profile;
    }

    @Override
    protected String getIdleSound() {
        return "random.breathe";
    }

    @Override
    protected void makeStepSound() {
        this.makeSound("step.grass", 0.15F, 1.0F);
    }

    @Override
    protected String getDeathSound() {
        return "random.classic_hurt";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.REGULAR;
    }
}
