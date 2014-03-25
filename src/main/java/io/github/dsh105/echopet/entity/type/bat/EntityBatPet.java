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

package io.github.dsh105.echopet.entity.type.bat;

import io.github.dsh105.echopet.entity.*;
import net.minecraft.server.v1_7_R2.MathHelper;
import net.minecraft.server.v1_7_R2.World;

@EntitySize(width = 0.5F, height = 0.9F)
@EntityPetType(petType = PetType.BAT)
public class EntityBatPet extends EntityPet {

    public EntityBatPet(World world) {
        super(world);
    }

    public EntityBatPet(World world, Pet pet) {
        super(world, pet);
    }

    public void setHanging(boolean flag) {
        byte b0 = this.datawatcher.getByte(16);
        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 1)));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & -2)));
        }
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, new Byte((byte) 0));
    }

    @Override
    protected String getIdleSound() {
        return this.isStartled() && this.random.nextInt(4) != 0 ? null : "mob.bat.idle";
    }

    @Override
    public void onLive() {
        super.onLive();
        if (this.isStartled()) {
            this.motX = this.motY = this.motZ = 0.0D;
            this.locY = (double) MathHelper.floor(this.locY) + 1.0D - (double) this.length;
        } else {
            this.motY *= 0.6000000238418579D;
        }
    }

    public boolean isStartled() {
        return (this.datawatcher.getByte(16) & 1) != 0;
    }

    @Override
    protected String getDeathSound() {
        return "mob.bat.death";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.TINY;
    }
}