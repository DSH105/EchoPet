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

package com.dsh105.echopetv3.nms.v1_7_R3.entity.type;

import com.dsh105.echopetv3.api.entity.entitypet.type.EntityEnderDragonPet;
import com.dsh105.echopetv3.api.entity.pet.type.EnderDragonPet;
import com.dsh105.echopetv3.nms.v1_7_R3.entity.EntityPetBase;
import net.minecraft.server.v1_7_R3.*;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;

import java.util.Iterator;
import java.util.List;

// TODO: ew, this needs to be 'humanised' so that it's readable
public class EntityEnderDragonPetBase extends EntityPetBase<EnderDragonPet> implements EntityEnderDragonPet, IComplex {

    private double i;
    private double h;
    private double j;
    public int bo = -1;
    public double[][] bn = new double[64][3];
    private EntityComplexPart[] children;
    private EntityComplexPart head;
    private EntityComplexPart body;
    private EntityComplexPart tail1;
    private EntityComplexPart tail2;
    private EntityComplexPart tail3;
    private EntityComplexPart wing1;
    private EntityComplexPart wing2;
    private float bx;
    private float by;
    private boolean bz;
    private boolean bA;
    private int bB;
    private Entity bD;

    public EntityEnderDragonPetBase(World world, EnderDragonPet pet) {
        super(world, pet);
        this.children = new EntityComplexPart[]{this.head = new EntityComplexPart(this, "head", 6.0F, 6.0F), this.body = new EntityComplexPart(this, "body", 8.0F, 8.0F), this.tail1 = new EntityComplexPart(this, "tail", 4.0F, 4.0F), this.tail2 = new EntityComplexPart(this, "tail", 4.0F, 4.0F), this.tail3 = new EntityComplexPart(this, "tail", 4.0F, 4.0F), this.wing1 = new EntityComplexPart(this, "wing", 4.0F, 4.0F), this.wing2 = new EntityComplexPart(this, "wing", 4.0F, 4.0F)};
        this.setNoClipEnabled(true);
        this.i = 100.0D;
        this.al = true;
    }

    public double[] b(int i, float f) {
        if (this.getHealth() <= 0.0F) {
            f = 0.0F;
        }

        f = 1.0F - f;
        int j = this.bo - i * 1 & 63;
        int k = this.bo - i * 1 - 1 & 63;
        double[] adouble = new double[3];
        double d0 = this.bn[j][0];
        double d1 = MathHelper.g(this.bn[k][0] - d0);

        adouble[0] = d0 + d1 * (double) f;
        d0 = this.bn[j][1];
        d1 = this.bn[k][1] - d0;
        adouble[1] = d0 + d1 * (double) f;
        adouble[2] = this.bn[j][2] + (this.bn[k][2] - this.bn[j][2]) * (double) f;
        return adouble;
    }

    @Override
    public void e() {
        if (getPet().isStationary()) {
            return;
        }

        float f;
        float f1;

        if (getPassenger() != null && getPassenger() == getPet().getOwner()) {
            getPet().onRide(0F, 0F);
            return;
        }

        if (this.world.isStatic) {
            f = MathHelper.cos(this.by * 3.1415927F * 2.0F);
            f1 = MathHelper.cos(this.bx * 3.1415927F * 2.0F);
            if (f1 <= -0.3F && f >= -0.3F) {
                this.world.a(this.locX, this.locY, this.locZ, "mob.enderdragon.wings", 5.0F, 0.8F + this.random.nextFloat() * 0.3F, false);
            }
        }

        this.bx = this.by;
        float f2;

        if (this.getHealth() <= 0.0F) {
            f = (this.random.nextFloat() - 0.5F) * 8.0F;
            f1 = (this.random.nextFloat() - 0.5F) * 4.0F;
            f2 = (this.random.nextFloat() - 0.5F) * 8.0F;
            this.world.addParticle("largeexplode", this.locX + (double) f, this.locY + 2.0D + (double) f1, this.locZ + (double) f2, 0.0D, 0.0D, 0.0D);
        } else {
            // For EnderCrystals
            //this.bN();

            f = 0.2F / (MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 10.0F + 1.0F);
            f *= (float) Math.pow(2.0D, this.motY);
            if (this.bA) {
                this.by += f * 0.5F;
            } else {
                this.by += f;
            }

            this.yaw = MathHelper.g(this.yaw);
            if (this.bo < 0) {
                for (int d05 = 0; d05 < this.bn.length; ++d05) {
                    this.bn[d05][0] = (double) this.yaw;
                    this.bn[d05][1] = this.locY;
                }
            }

            if (++this.bo == this.bn.length) {
                this.bo = 0;
            }

            this.bn[this.bo][0] = (double) this.yaw;
            this.bn[this.bo][1] = this.locY;
            double d0;
            double d1;
            double d2;
            double d3;
            float f3;

            if (this.world.isStatic) {
                if (this.bh > 0) {
                    d0 = this.locX + (this.bi - this.locX) / (double) this.bg;
                    d1 = this.locY + (this.bj - this.locY) / (double) this.bg;
                    d2 = this.locZ + (this.bk - this.locZ) / (double) this.bg;
                    d3 = MathHelper.g(this.bl - (double) this.yaw);
                    this.yaw = (float) ((double) this.yaw + d3 / (double) this.bg);
                    this.pitch = (float) ((double) this.pitch + (this.bl - (double) this.pitch) / (double) this.bg);
                    --this.bh;
                    this.setPosition(d0, d1, d2);
                    this.b(this.yaw, this.pitch);
                }
            } else {
                d0 = this.h - this.locX;
                d1 = (this.i + 5.0D) - this.locY;
                d2 = this.j - this.locZ;
                d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (this.bD != null) {
                    this.h = this.bD.locX;
                    this.j = this.bD.locZ;
                    double d4 = this.h - this.locX;
                    double d5 = this.j - this.locZ;
                    double d6 = Math.sqrt(d4 * d4 + d5 * d5);
                    double d7 = 0.4000000059604645D + d6 / 80.0D - 1.0D;

                    if (d7 > 10.0D) {
                        d7 = 10.0D;
                    }

                    this.i = this.bD.boundingBox.b + d7;
                } else {
                    this.h += this.random.nextGaussian() * 2.0D;
                    this.j += this.random.nextGaussian() * 2.0D;
                }

                if (this.bz || d3 < 100.0D || d3 > 22500.0D || this.positionChanged || this.F) {
                    this.target();
                }

                d1 /= (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
                f3 = 0.6F;
                if (d1 < (double) (-f3)) {
                    d1 = (double) (-f3);
                }

                if (d1 > (double) f3) {
                    d1 = (double) f3;
                }

                this.motY += d1 * 0.10000000149011612D;
                this.yaw = MathHelper.g(this.yaw);
                double d8 = 180.0D - Math.atan2(d0, d2) * 180.0D / 3.1415927410125732D;
                double d9 = MathHelper.g(d8 - (double) this.yaw);

                if (d9 > 50.0D) {
                    d9 = 50.0D;
                }

                if (d9 < -50.0D) {
                    d9 = -50.0D;
                }

                Vec3D vec3d = Vec3D.a(this.h - this.locX, this.i - this.locY, this.j - this.locZ).a();
                Vec3D vec3d1 = Vec3D.a((double) MathHelper.sin(this.yaw * 3.1415927F / 180.0F), this.motY, (double) (-MathHelper.cos(this.yaw * 3.1415927F / 180.0F))).a();
                float f4 = (float) (vec3d1.b(vec3d) + 0.5D) / 1.5F;

                if (f4 < 0.0F) {
                    f4 = 0.0F;
                }

                this.bf *= 0.8F;
                float f5 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 1.0F + 1.0F;
                double d10 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 1.0D + 1.0D;

                if (d10 > 40.0D) {
                    d10 = 40.0D;
                }

                this.bf = (float) ((double) this.bg + d9 * (0.699999988079071D / d10 / (double) f5));
                this.yaw += this.bf * 0.1F;
                float f6 = (float) (2.0D / (d10 + 1.0D));
                float f7 = 0.06F;

                this.a(0.0F, -1.0F, f7 * (f4 * f6 + (1.0F - f6)));
                if (this.bA) {
                    this.move(this.motX * 0.800000011920929D, this.motY * 0.800000011920929D, this.motZ * 0.800000011920929D);
                } else {
                    this.move(this.motX, this.motY, this.motZ);
                }

                Vec3D vec3d2 = Vec3D.a(this.motX, this.motY, this.motZ).a();
                float f8 = (float) (vec3d2.b(vec3d1) + 1.0D) / 2.0F;

                f8 = 0.8F + 0.15F * f8;
                this.motX *= (double) f8;
                this.motZ *= (double) f8;
                this.motY *= 0.9100000262260437D;
            }

            this.updateComplexParts();

            //Nope, absolutely no destruction
            /*if (!this.world.isStatic) {
                this.bA = this.a(this.head.boundingBox) | this.a(this.body.boundingBox);
			}*/
        }
    }

    @Override
    public void updatePosition(double x, double y, double z) {
        double[] d0 = new double[]{x, y, z};
        double[] d1 = new double[]{this.locX, this.locY, this.locZ};
        for (int i = 0; i < 3; i++) {
            if (this.world.getWorld().getBlockAt((int) x, (int) y, (int) z).getType().isSolid()) {
                d0[i] = d1[i];
            }
        }
        this.setPosition(d0[0], d0[1], d0[2]);
    }

    @Override
    public void updateComplexParts() {
        if (this.children != null) {
            this.aN = this.yaw;
            this.head.width = this.head.length = 3.0F;
            this.tail1.width = this.tail1.length = 2.0F;
            this.tail2.width = this.tail2.length = 2.0F;
            this.tail3.width = this.tail3.length = 2.0F;
            this.body.length = 3.0F;
            this.body.width = 5.0F;
            this.wing1.length = 2.0F;
            this.wing1.width = 4.0F;
            this.wing2.length = 3.0F;
            this.wing2.width = 4.0F;
            float f1 = (float) (this.b(5, 1.0F)[1] - this.b(10, 1.0F)[1]) * 10.0F / 180.0F * 3.1415927F;
            float f2 = MathHelper.cos(f1);
            float f9 = -MathHelper.sin(f1);
            float f10 = this.yaw * 3.1415927F / 180.0F;
            float f11 = MathHelper.sin(f10);
            float f12 = MathHelper.cos(f10);

            this.body.h();
            this.body.setPositionRotation(this.locX + (double) (f11 * 0.5F), this.locY, this.locZ - (double) (f12 * 0.5F), 0.0F, 0.0F);
            this.wing1.h();
            this.wing1.setPositionRotation(this.locX + (double) (f12 * 4.5F), this.locY + 2.0D, this.locZ + (double) (f11 * 4.5F), 0.0F, 0.0F);
            this.wing2.h();
            this.wing2.setPositionRotation(this.locX - (double) (f12 * 4.5F), this.locY + 2.0D, this.locZ - (double) (f11 * 4.5F), 0.0F, 0.0F);

		/*if (!this.world.isStatic && this.hurtTicks == 0) {
                PetGoalAttack attackGoal = (PetGoalAttack) this.petGoalSelector.getGoal(PetGoalAttack.class);
				if (attackGoal != null && attackGoal.isActive) {
					this.launchEntities(this.world.getEntities(this, this.wing1.boundingBox.grow(4.0D, 2.0D, 4.0D).d(0.0D, -2.0D, 0.0D)));
					this.launchEntities(this.world.getEntities(this, this.wing2.boundingBox.grow(4.0D, 2.0D, 4.0D).d(0.0D, -2.0D, 0.0D)));
					this.damageEntities(this.world.getEntities(this, this.head.boundingBox.grow(1.0D, 1.0D, 1.0D)));
				}
		}*/

            double[] adouble = this.b(5, 1.0F);
            double[] adouble1 = this.b(0, 1.0F);

            float f3 = MathHelper.sin(this.yaw * 3.1415927F / 180.0F - this.bf * 0.01F);
            float f13 = MathHelper.cos(this.yaw * 3.1415927F / 180.0F - this.bf * 0.01F);

            this.head.h();
            this.head.setPositionRotation(this.locX + (double) (f3 * 5.5F * f2), this.locY + (adouble1[1] - adouble[1]) * 1.0D + (double) (f9 * 5.5F), this.locZ - (double) (f13 * 5.5F * f2), 0.0F, 0.0F);

            for (int j = 0; j < 3; ++j) {
                EntityComplexPart entitycomplexpart = null;

                if (j == 0) {
                    entitycomplexpart = this.tail1;
                }

                if (j == 1) {
                    entitycomplexpart = this.tail2;
                }

                if (j == 2) {
                    entitycomplexpart = this.tail3;
                }

                double[] adouble2 = this.b(12 + j * 2, 1.0F);
                float f14 = this.yaw * 3.1415927F / 180.0F + (float) MathHelper.g(adouble2[0] - adouble[0]) * 3.1415927F / 180.0F * 1.0F;
                float f15 = MathHelper.sin(f14);
                float f16 = MathHelper.cos(f14);
                float f17 = 1.5F;
                float f18 = (float) (j + 1) * 2.0F;

                entitycomplexpart.h();
                entitycomplexpart.setPositionRotation(this.locX - (double) ((f11 * f17 + f15 * f18) * f2), this.locY + (adouble2[1] - adouble[1]) * 1.0D - (double) ((f18 + f17) * f9) + 1.5D, this.locZ + (double) ((f12 * f17 + f16 * f18) * f2), 0.0F, 0.0F);
            }
        }
    }

    //a(List)
    private void launchEntities(List list) {
        double d0 = (this.body.boundingBox.a + this.body.boundingBox.d) / 2.0D;
        double d1 = (this.body.boundingBox.c + this.body.boundingBox.f) / 2.0D;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            if (entity instanceof EntityLiving && !(entity instanceof EntityPlayer)) {
                double d2 = entity.locX - d0;
                double d3 = entity.locZ - d1;
                double d4 = d2 * d2 + d3 * d3;

                entity.g(d2 / d4 * 4.0D, 0.20000000298023224D, d3 / d4 * 4.0D);
            }
        }
    }

    @Override
    public void setPosition(double d0, double d1, double d2) {
        this.locX = d0;
        this.locY = d1;
        this.locZ = d2;
        float f = this.width / 2.0F;
        float f1 = this.length;

        this.boundingBox.b(d0 - (double) f, d1 - (double) this.height + (double) this.W, d2 - (double) f, d0 + (double) f, d1 - (double) this.height + (double) this.W + (double) f1, d2 + (double) f);
    }

    //b(List)
    private void damageEntities(List list) {
        for (int i = 0; i < list.size(); ++i) {
            Entity entity = (Entity) list.get(i);

            if (entity instanceof EntityLiving) {
                this.attack((LivingEntity) entity.getBukkitEntity());
            }
        }
    }

    //bO() EntityEnderDragon
    protected void target() {
        this.bz = false;
        if (this.random.nextInt(2) == 0 && !this.world.players.isEmpty()) {
            if (this.random.nextInt(50) <= 40 && this.getPet().getOwner() != null) {
                this.bD = ((CraftPlayer) this.getPet().getOwner()).getHandle();
            } else {
                this.bD = (Entity) this.world.players.get(this.random.nextInt(this.world.players.size()));
            }
        } else {
            boolean flag;

            do {
                this.h = 0.0D;
                this.i = (double) (70.0F + this.random.nextFloat() * 50.0F);
                this.j = 0.0D;
                this.h += (double) (this.random.nextFloat() * 120.0F - 60.0F);
                this.j += (double) (this.random.nextFloat() * 120.0F - 60.0F);
                double d0 = this.locX - this.h;
                double d1 = this.locY - this.i;
                double d2 = this.locZ - this.j;

                flag = d0 * d0 + d1 * d1 + d2 * d2 > 100.0D;
            } while (!flag);

            this.bD = null;
        }
    }

    @Override
    public World a() {
        return this.world;
    }

    @Override
    public boolean a(EntityComplexPart entityComplexPart, DamageSource damageSource, float f) {
        if (entityComplexPart != this.head) {
            f = f / 4.0F + 1.0F;
        }

        float f1 = this.yaw * 3.1415927F / 180.0F;
        float f2 = MathHelper.sin(f1);
        float f3 = MathHelper.cos(f1);

        this.h = this.locX + (double) (f2 * 5.0F) + (double) ((this.random.nextFloat() - 0.5F) * 2.0F);
        this.i = this.locY + (double) (this.random.nextFloat() * 3.0F) + 1.0D;
        this.j = this.locZ - (double) (f3 * 5.0F) + (double) ((this.random.nextFloat() - 0.5F) * 2.0F);
        this.bD = null;
        if (damageSource.getEntity() instanceof EntityHuman || damageSource.isExplosion()) {
            //this.attack(damageSource.getEntity(), f);
        }

        return true;
    }
}