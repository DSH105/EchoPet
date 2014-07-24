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

package com.dsh105.echopet.nms.v1_7_R1.entity.type;

import com.dsh105.echopet.api.entity.entitypet.type.EntityHorsePet;
import com.dsh105.echopet.api.entity.pet.type.HorsePet;
import com.dsh105.echopet.nms.v1_7_R1.entity.EntityAgeablePetBase;
import net.minecraft.server.v1_7_R1.Block;
import net.minecraft.server.v1_7_R1.Blocks;
import net.minecraft.server.v1_7_R1.StepSound;
import net.minecraft.server.v1_7_R1.World;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;

public class EntityHorsePetBase extends EntityAgeablePetBase<HorsePet> implements EntityHorsePet {

    private int reverseMotion;

    public EntityHorsePetBase(World world, HorsePet pet) {
        super(world, pet);
    }

    @Override
    public void setBaby(boolean flag) {
        if (flag) {
            this.datawatcher.watch(12, -24000);
        } else {
            this.datawatcher.watch(12, 0);
        }
    }

    @Override
    public void setVariant(Horse.Variant variant) {
        if (variant != Horse.Variant.HORSE) {
            setArmour(HorsePet.Armour.NONE);
        }
        this.datawatcher.watch(DATAWATCHER_HORSE_VARIANT, (byte) variant.ordinal());
    }

    @Override
    public void setColor(Horse.Color color) {
        this.datawatcher.watch(DATAWATCHER_HORSE_SKIN, color.ordinal() & 0xFF | getStyle().ordinal() << 8);
    }

    @Override
    public void setStyle(Horse.Style style) {
        this.datawatcher.watch(DATAWATCHER_HORSE_SKIN, getColor().ordinal() & 0xFF | style.ordinal() << 8);
    }

    @Override
    public void setArmour(HorsePet.Armour armour) {
        if (getVariant() == Horse.Variant.HORSE) {
            this.datawatcher.watch(DATAWATCHER_HORSE_ARMOUR, armour.ordinal());
        }
    }

    @Override
    public void setSaddled(boolean flag) {
        animation(ANIMATION_SADDLE, flag);
    }

    @Override
    public void setChested(boolean flag) {
        animation(ANIMATION_CHEST, flag);
    }

    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.values()[this.datawatcher.getByte(DATAWATCHER_HORSE_VARIANT)];
    }

    @Override
    public Horse.Color getColor() {
        return Horse.Color.values()[this.datawatcher.getByte(DATAWATCHER_HORSE_SKIN) & 0xFF];
    }

    @Override
    public Horse.Style getStyle() {
        return Horse.Style.values()[this.datawatcher.getByte(DATAWATCHER_HORSE_SKIN) >>> 8];
    }

    @Override
    public HorsePet.Armour getArmour() {
        return HorsePet.Armour.values()[this.datawatcher.getByte(DATAWATCHER_HORSE_ARMOUR)];
    }

    @Override
    public boolean isSaddled() {
        return (this.datawatcher.getByte(DATAWATCHER_ANIMATION) & ANIMATION_SADDLE) != 0;
    }

    @Override
    public boolean isChested() {
        return (this.datawatcher.getByte(DATAWATCHER_ANIMATION) & ANIMATION_CHEST) != 0;
    }

    @Override
    public void animation(int animationId, boolean flag) {
        int value = this.datawatcher.getInt(16);

        if (flag) {
            this.datawatcher.watch(DATAWATCHER_ANIMATION, Integer.valueOf(value | animationId));
        } else {
            this.datawatcher.watch(DATAWATCHER_ANIMATION, Integer.valueOf(value & ~animationId));
        }
    }

    @Override
    public boolean attack(LivingEntity entity, float damage) {
        boolean flag = super.attack(entity, damage);
        if (flag) {
            animation(ANIMATION_REAR, true);
            if (getVariant() == Horse.Variant.HORSE) {
                this.makeSound("mob.horse.angry", 1.0F, 1.0F);
            } else if (getVariant() == Horse.Variant.DONKEY || getVariant() == Horse.Variant.MULE) {
                this.makeSound("mob.horse.donkey.angry", 1.0F, 1.0F);
            }
        }
        return flag;
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(DATAWATCHER_ANIMATION, Integer.valueOf(0));
        this.datawatcher.a(DATAWATCHER_HORSE_VARIANT, Byte.valueOf((byte) 0));
        this.datawatcher.a(DATAWATCHER_HORSE_SKIN, Integer.valueOf(0));
        this.datawatcher.a(DATAWATCHER_OWNER_NAME, getPet().getOwnerName());
        this.datawatcher.a(DATAWATCHER_HORSE_ARMOUR, Integer.valueOf(0));
    }

    @Override
    protected void makeStepSound(int i, int j, int k, Block block) {
        StepSound stepsound = block.stepSound;

        if (this.world.getType(i, j + 1, k) == Blocks.SNOW) {
            stepsound = Blocks.SNOW.stepSound;
        }

        if (!block.getMaterial().isLiquid()) {
            Horse.Variant variant = this.getVariant();

            if (this.passenger != null && variant != Horse.Variant.DONKEY && variant != Horse.Variant.MULE) {
                ++this.reverseMotion;
                if (this.reverseMotion > 5 && this.reverseMotion % 3 == 0) {
                    this.makeSound("mob.horse.gallop", stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
                    if (variant == Horse.Variant.HORSE && this.random.nextInt(10) == 0) {
                        this.makeSound("mob.horse.breathe", stepsound.getVolume1() * 0.6F, stepsound.getVolume2());
                    }
                } else if (this.reverseMotion <= 5) {
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
    public void e(float sideMotion, float forwardMotion) {
        super.e(sideMotion, forwardMotion);
        if (forwardMotion <= 0.0F) {
            this.reverseMotion = 0;
        }
    }
}