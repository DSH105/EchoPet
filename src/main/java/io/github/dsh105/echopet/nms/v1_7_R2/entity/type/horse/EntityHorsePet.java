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

package io.github.dsh105.echopet.nms.v1_7_R2.entity.type.horse;

import io.github.dsh105.echopet.nms.v1_7_R2.entity.*;
import net.minecraft.server.v1_7_R2.*;

@EntitySize(width = 1.4F, height = 1.6F)
@EntityPetType(petType = PetType.HORSE)
public class EntityHorsePet extends EntityAgeablePet {

    int bP = 0;

    public EntityHorsePet(World world) {
        super(world);
    }

    public EntityHorsePet(World world, Pet pet) {
        super(world, pet);
    }

    @Override
    public boolean isBaby() {
        return ((HorsePet) pet).isBaby();
    }

    @Override
    public void setBaby(boolean flag) {
        if (flag) {
            this.datawatcher.watch(12, Integer.valueOf(-24000));
        } else {
            this.datawatcher.watch(12, new Integer(0));
        }
    }

    public void setSaddled(boolean flag) {
        this.horseVisual(4, flag);
        ((HorsePet) pet).saddle = flag;
    }

    public void setType(HorseType t) {
        if (t != HorseType.NORMAL) {
            this.setArmour(HorseArmour.NONE);
        }
        this.datawatcher.watch(19, Byte.valueOf((byte) t.getId()));
    }

    public void setVariant(HorseVariant v, HorseMarking m) {
        this.datawatcher.watch(20, Integer.valueOf(m.getId(v)));
    }

    public void setArmour(HorseArmour a) {
        if (this.datawatcher.getByte(19) == Byte.valueOf((byte) HorseType.NORMAL.getId())) {
            this.datawatcher.watch(22, Integer.valueOf(a.getId()));
        }
    }

    public void setChested(boolean flag) {
        this.horseVisual(8, flag);
    }

    @Override
    public boolean attack(Entity entity) {
        boolean flag = super.attack(entity);
        if (flag) {
            horseVisual(64, true);
            if (getType() == 0) {
                this.makeSound("mob.horse.angry", 1.0F, 1.0F);
            } else if (getType() == 2 || getType() == 3) {
                this.makeSound("mob.horse.donkey.angry", 1.0F, 1.0F);
            }
        }
        return flag;
    }

    /*
        * 4 = saddle
        * 8 = chest
        * 32 = head down
        * 64 = rear
        * 128 = mouth open
        */
    private void horseVisual(int i, boolean flag) {
        int j = this.datawatcher.getInt(16);

        if (flag) {
            this.datawatcher.watch(16, Integer.valueOf(j | i));
        } else {
            this.datawatcher.watch(16, Integer.valueOf(j & ~i));
        }
    }

    public int getType() {
        return this.datawatcher.getByte(19);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, Integer.valueOf(0));
        this.datawatcher.a(19, Byte.valueOf((byte) 0));
        this.datawatcher.a(20, Integer.valueOf(0));
        this.datawatcher.a(21, String.valueOf(""));
        this.datawatcher.a(22, Integer.valueOf(0));
    }

    @Override
    protected String getIdleSound() {
        int i = this.getType();

        return i == 3 ? "mob.horse.zombie.idle" : (i == 4 ? "mob.horse.skeleton.idle" : (i != 1 && i != 2 ? "mob.horse.idle" : "mob.horse.donkey.idle"));
    }

    @Override
    protected void makeStepSound(int i, int j, int k, Block block) {
        StepSound stepsound = block.stepSound;

        if (this.world.getType(i, j + 1, k) == Blocks.SNOW) {
            stepsound = Blocks.SNOW.stepSound;
        }

        if (!block.getMaterial().isLiquid()) {
            int l = this.getType();

            if (this.passenger != null && l != 1 && l != 2) {
                ++this.bP;
                if (this.bP > 5 && this.bP % 3 == 0) {
                    this.makeSound("mob.horse.gallop", stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
                    if (l == 0 && this.random.nextInt(10) == 0) {
                        this.makeSound("mob.horse.breathe", stepsound.getVolume1() * 0.6F, stepsound.getVolume2());
                    }
                } else if (this.bP <= 5) {
                    this.makeSound("mob.horse.wood", stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
                }
            } else if (stepsound == Block.f) {
                this.makeSound("mob.horse.wood", stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
            } else {
                this.makeSound("mob.horse.soft", stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
            }
        }
    }

    @Override
    public void e(float sideMot, float forwMot) {
        super.e(sideMot, forwMot);
        if (forwMot <= 0.0F) {
            this.bP = 0;
        }
    }

    @Override
    protected String getDeathSound() {
        int i = this.getType();
        return i == 3 ? "mob.horse.zombie.death" : (i == 4 ? "mob.horse.skeleton.death" : (i != 1 && i != 2 ? "mob.horse.death" : "mob.horse.donkey.death"));
    }

    @Override
    public SizeCategory getSizeCategory() {
        if (this.isBaby()) {
            return SizeCategory.TINY;
        } else {
            return SizeCategory.LARGE;
        }
    }
}