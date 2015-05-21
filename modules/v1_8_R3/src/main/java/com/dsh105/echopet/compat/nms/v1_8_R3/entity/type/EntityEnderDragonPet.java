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

package com.dsh105.echopet.compat.nms.v1_8_R2.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityEnderDragonPet;
import com.dsh105.echopet.compat.api.event.PetRideJumpEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.Logger;
import com.dsh105.echopet.compat.nms.v1_8_R2.entity.EntityNoClipPet;
import com.google.common.collect.Lists;
import net.minecraft.server.v1_8_R2.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;

@EntitySize(width = 16.0F, height = 8.0F)
@EntityPetType(petType = PetType.ENDERDRAGON)
public class EntityEnderDragonPet extends EntityNoClipPet implements IComplex, IMonster, IEntityEnderDragonPet {

    // TODO: ugly :(

    private double a;
    private double b;
    private double c;
    public int bl = -1;
    public double[][] bk = new double[64][3];
    private EntityComplexPart[] children;
    private EntityComplexPart head;
    private EntityComplexPart body;
    private EntityComplexPart tail1;
    private EntityComplexPart tail2;
    private EntityComplexPart tail3;
    private EntityComplexPart wing1;
    private EntityComplexPart wing2;
    private float bu;
    private float bv;
    private boolean bw;
    private boolean bx;
    private int by;
    private Entity bA;

    public EntityEnderDragonPet(World world) {
        super(world);
    }

    public EntityEnderDragonPet(World world, IPet pet) {
        super(world, pet);
        this.children = new EntityComplexPart[]{this.head = new EntityComplexPart(this, "head", 6.0F, 6.0F), this.body = new EntityComplexPart(this, "body", 8.0F, 8.0F), this.tail1 = new EntityComplexPart(this, "tail", 4.0F, 4.0F), this.tail2 = new EntityComplexPart(this, "tail", 4.0F, 4.0F), this.tail3 = new EntityComplexPart(this, "tail", 4.0F, 4.0F), this.wing1 = new EntityComplexPart(this, "wing", 4.0F, 4.0F), this.wing2 = new EntityComplexPart(this, "wing", 4.0F, 4.0F)};
        this.noClip(true);
        this.b = 100.0D;
        this.ah = true;
    }

    @Override
    public void resizeBoundingBox(boolean flag) {
        this.setSize(flag ? 8.0F : 16.0F, flag ? 4.0F : 8.0F);
    }

    public double[] b(int i, float f) {
        if (this.getHealth() <= 0.0F) {
            f = 0.0F;
        }

        f = 1.0F - f;
        int j = this.bl - i * 1 & 63;
        int k = this.bl - i * 1 - 1 & 63;
        double[] adouble = new double[3];
        double d0 = this.bk[j][0];
        double d1 = MathHelper.g(this.bk[k][0] - d0);

        adouble[0] = d0 + d1 * (double) f;
        d0 = this.bk[j][1];
        d1 = this.bk[k][1] - d0;
        adouble[1] = d0 + d1 * (double) f;
        adouble[2] = this.bk[j][2] + (this.bk[k][2] - this.bk[j][2]) * (double) f;
        return adouble;
    }

    @Override
    public void m() {
        float f;
        float f1;

        if (this.passenger != null && (this.passenger instanceof EntityHuman)) {
            EntityHuman human = (EntityHuman) this.passenger;
            if (human.getBukkitEntity() == this.getPlayerOwner().getPlayer()) {
                float forw = ((EntityLiving) this.passenger).ba;
                float side = ((EntityLiving) this.passenger).aZ;

                Vector v = new Vector();
                Location l = new Location(this.world.getWorld(), this.locX, this.locY, this.locZ);

                if (side < 0.0F) {
                    l.setYaw(this.passenger.yaw - 90);
                    v.add(l.getDirection().normalize().multiply(-0.5));
                } else if (side > 0.0F) {
                    l.setYaw(this.passenger.yaw + 90);
                    v.add(l.getDirection().normalize().multiply(-0.5));
                }

                if (forw < 0.0F) {
                    l.setYaw(this.passenger.yaw);
                    v.add(l.getDirection().normalize().multiply(0.5));
                } else if (forw > 0.0F) {
                    l.setYaw(this.passenger.yaw);
                    v.add(l.getDirection().normalize().multiply(0.5));
                }

                this.lastYaw = this.yaw = this.passenger.yaw - 180;
                this.pitch = this.passenger.pitch * 0.5F;
                this.setYawPitch(this.yaw, this.pitch);
                this.aI = this.aG = this.yaw;

                if (this.FIELD_JUMP != null) {
                    try {
                        if (this.FIELD_JUMP.getBoolean(this.passenger)) {
                            PetRideJumpEvent rideEvent = new PetRideJumpEvent(this.getPet(), this.jumpHeight);
                            EchoPet.getPlugin().getServer().getPluginManager().callEvent(rideEvent);
                            if (!rideEvent.isCancelled()) {
                                v.setY(0.5F);
                            }
                        } else {
                            if (((EntityLiving) this.passenger).pitch >= 50) {
                                v.setY(-0.4F);
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        Logger.log(Logger.LogLevel.WARNING, "Failed to initiate LivingPet Flying Motion for " + this.getPlayerOwner().getName() + "'s LivingPet.", e, true);
                    } catch (IllegalAccessException e) {
                        Logger.log(Logger.LogLevel.WARNING, "Failed to initiate LivingPet Flying Motion for " + this.getPlayerOwner().getName() + "'s LivingPet.", e, true);
                    } catch (IllegalStateException e) {
                        Logger.log(Logger.LogLevel.WARNING, "Failed to initiate LivingPet Flying Motion for " + this.getPlayerOwner().getName() + "'s LivingPet.", e, true);
                    }
                }

                l.add(v.multiply(Math.pow(this.rideSpeed, this.rideSpeed)));
                this.setPos(l.getX(), l.getY(), l.getZ());
                this.updateComplexParts();
                return;
            }
        }

        if (this.world.isClientSide) {
            f = MathHelper.cos(this.bv * 3.1415927F * 2.0F);
            f1 = MathHelper.cos(this.bu * 3.1415927F * 2.0F);
            if (f1 <= -0.3F && f >= -0.3F && !this.R()) {
                this.world.a(this.locX, this.locY, this.locZ, "mob.enderdragon.wings", 5.0F, 0.8F + this.random.nextFloat() * 0.3F, false);
            }
        }

        this.bu = this.bv;
        float f2;

        if (this.getHealth() <= 0.0F) {
            f = (this.random.nextFloat() - 0.5F) * 8.0F;
            f1 = (this.random.nextFloat() - 0.5F) * 4.0F;
            f2 = (this.random.nextFloat() - 0.5F) * 8.0F;
            this.world.addParticle(EnumParticle.EXPLOSION_LARGE, this.locX + (double) f, this.locY + 2.0D + (double) f1, this.locZ + (double) f2, 0.0D, 0.0D, 0.0D, new int[0]);
        } else {
            // For EnderCrystals
            //this.n();

            f = 0.2F / (MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 10.0F + 1.0F);
            f *= (float) Math.pow(2.0D, this.motY);
            if (this.bx) {
                this.bv += f * 0.5F;
            } else {
                this.bv += f;
            }

            this.yaw = MathHelper.g(this.yaw);
            if (ce()) {
                this.bv = 0.5F;
            } else {
                if (this.bl < 0) {
                    for (int i = 0; i < this.bk.length; ++i) {
                        this.bk[i][0] = (double) this.yaw;
                        this.bk[i][1] = this.locY;
                    }
                }
                
                if (++this.bl == this.bk.length) {
                    this.bl = 0;
                }
            }


            this.bk[this.bl][0] = (double) this.yaw;
            this.bk[this.bl][1] = this.locY;

            if (this.world.isClientSide) {
                if (this.bc > 0) {
                    double d3 = this.locX + (this.bd - this.locX) / this.bc;
                    double d0 = this.locY + (this.be - this.locY) / this.bc;
                    double d1 = this.locZ + (this.bf - this.locZ) / this.bc;
                    double d2 = MathHelper.g(this.bg - this.yaw);
                    this.yaw = ((float) (this.yaw + d2 / this.bc));
                    this.pitch = ((float) (this.pitch + (this.bh - this.pitch) / this.bc));
                    this.bc -= 1;
                    setPosition(d3, d0, d1);
                    setYawPitch(this.yaw, this.pitch);
                }
            } else {
                double d3 = this.a - this.locX;
                double d0 = this.b - this.locY;
                double d1 = this.c - this.locZ;
                double d2 = d3 * d3 + d0 * d0 + d1 * d1;
                if (this.bA != null) {
                    this.a = this.bA.locX;
                    this.c = this.bA.locZ;
                    double d5 = this.a - this.locX;
                    double d6 = this.c - this.locZ;
                    double d7 = Math.sqrt(d5 * d5 + d6 * d6);

                    double d4 = 0.4000000059604645D + d7 / 80.0D - 1.0D;
                    if (d4 > 10.0D) {
                        d4 = 10.0D;
                    }
                    this.b = (this.bA.getBoundingBox().b + d4);
                } else {
                    this.a += this.random.nextGaussian() * 2.0D;
                    this.c += this.random.nextGaussian() * 2.0D;
                }
                if ((this.bw) || (d2 < 100.0D) || (d2 > 22500.0D) || (this.positionChanged) || (this.E)) {
                    target();
                }
                d0 /= MathHelper.sqrt(d3 * d3 + d1 * d1);
                float f3 = 0.6F;
                d0 = MathHelper.a(d0, -f3, f3);
                this.motY += d0 * 0.10000000149011612D;
                this.yaw = MathHelper.g(this.yaw);
                double d8 = 180.0D - MathHelper.b(d3, d1) * 180.0D / 3.1415927410125732D;
                double d9 = MathHelper.g(d8 - this.yaw);
                if (d9 > 50.0D) {
                    d9 = 50.0D;
                }
                if (d9 < -50.0D) {
                    d9 = -50.0D;
                }
                Vec3D vec3d = new Vec3D(this.a - this.locX, this.b - this.locY, this.c - this.locZ).a();

                double d4 = -MathHelper.cos(this.yaw * 3.1415927F / 180.0F);
                Vec3D vec3d1 = new Vec3D(MathHelper.sin(this.yaw * 3.1415927F / 180.0F), this.motY, d4).a();
                float f4 = ((float) vec3d1.b(vec3d) + 0.5F) / 1.5F;
                if (f4 < 0.0F) {
                    f4 = 0.0F;
                }
                this.bb *= 0.8F;
                float f5 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 1.0F + 1.0F;
                double d10 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 1.0D + 1.0D;
                if (d10 > 40.0D) {
                    d10 = 40.0D;
                }
                this.bb = ((float) (this.bb + d9 * (0.699999988079071D / d10 / f5)));
                this.yaw += this.bb * 0.1F;
                float f6 = (float) (2.0D / (d10 + 1.0D));
                float f7 = 0.06F;

                a(0.0F, -1.0F, f7 * (f4 * f6 + (1.0F - f6)));
                if (this.bx) {
                    move(this.motX * 0.800000011920929D, this.motY * 0.800000011920929D, this.motZ * 0.800000011920929D);
                } else {
                    move(this.motX, this.motY, this.motZ);
                }
                Vec3D vec3d2 = new Vec3D(this.motX, this.motY, this.motZ).a();
                float f8 = ((float) vec3d2.b(vec3d1) + 1.0F) / 2.0F;

                f8 = 0.8F + 0.15F * f8;
                this.motX *= f8;
                this.motZ *= f8;
                this.motY *= 0.9100000262260437D;
            }

            this.updateComplexParts();

            //Nope, absolutely no destruction
            /*if (!this.world.isStatic) {
                this.bx = this.a(this.head.getBoundingBox()) | this.a(this.body.getBoundingBox());
			}*/
        }
    }

    private void setPos(double x, double y, double z) {
        double[] d0 = new double[]{x, y, z};
        double[] d1 = new double[]{this.locX, this.locY, this.locZ};
        for (int i = 0; i < 3; i++) {
            if (this.world.getWorld().getBlockAt((int) x, (int) y, (int) z).getType().isSolid()) {
                d0[i] = d1[i];
            }
        }
        this.setPosition(d0[0], d0[1], d0[2]);
    }

    private void updateComplexParts() {
        if (this.children != null) {
            this.aI = this.yaw;
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
            float f1 = (float)(b(5, 1.0F)[1] - b(10, 1.0F)[1]) * 10.0F / 180.0F * 3.1415927F;
            float f2 = MathHelper.cos(f1);
            float f9 = -MathHelper.sin(f1);
            float f10 = this.yaw * 3.1415927F / 180.0F;
            float f11 = MathHelper.sin(f10);
            float f12 = MathHelper.cos(f10);

            this.body.t_();
            this.body.setPositionRotation(this.locX + f11 * 0.5F, this.locY, this.locZ - f12 * 0.5F, 0.0F, 0.0F);
            this.wing1.t_();
            this.wing1.setPositionRotation(this.locX + f12 * 4.5F, this.locY + 2.0D, this.locZ + f11 * 4.5F, 0.0F, 0.0F);
            this.wing2.t_();
            this.wing2.setPositionRotation(this.locX - f12 * 4.5F, this.locY + 2.0D, this.locZ - f11 * 4.5F, 0.0F, 0.0F);
            /*if ((!this.world.isClientSide) && (this.hurtTicks == 0)) {
                launchEntities(this.world.getEntities(this, this.wing1.getBoundingBox().grow(4.0D, 2.0D, 4.0D).c(0.0D, -2.0D, 0.0D)));
                launchEntities(this.world.getEntities(this, this.wing2.getBoundingBox().grow(4.0D, 2.0D, 4.0D).c(0.0D, -2.0D, 0.0D)));
                damageEntities(this.world.getEntities(this, this.head.getBoundingBox().grow(1.0D, 1.0D, 1.0D)));
            }*/
            double[] adouble = b(5, 1.0F);
            double[] adouble1 = b(0, 1.0F);

            float f3 = MathHelper.sin(this.yaw * 3.1415927F / 180.0F - this.bb * 0.01F);
            float f13 = MathHelper.cos(this.yaw * 3.1415927F / 180.0F - this.bb * 0.01F);

            this.head.t_();
            this.head.setPositionRotation(this.locX + f3 * 5.5F * f2, this.locY + (adouble1[1] - adouble[1]) * 1.0D + f9 * 5.5F, this.locZ - f13 * 5.5F * f2, 0.0F, 0.0F);
            for (int j = 0; j < 3; j++) {
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
                double[] adouble2 = b(12 + j * 2, 1.0F);
                float f14 = this.yaw * 3.1415927F / 180.0F + ((float) MathHelper.g(adouble2[0] - adouble[0])) * 3.1415927F / 180.0F * 1.0F;
                float f15 = MathHelper.sin(f14);
                float f16 = MathHelper.cos(f14);
                float f17 = 1.5F;
                float f18 = (j + 1) * 2.0F;

                entitycomplexpart.t_();
                entitycomplexpart.setPositionRotation(this.locX - (f11 * f17 + f15 * f18) * f2, this.locY + (adouble2[1] - adouble[1]) * 1.0D - (f18 + f17) * f9 + 1.5D, this.locZ + (f12 * f17 + f16 * f18) * f2, 0.0F, 0.0F);
            }
        }
    }

    private void target() {
        this.bw = false;
        ArrayList arraylist = Lists.newArrayList(this.world.players);
        Iterator iterator = arraylist.iterator();
        while (iterator.hasNext()) {
            if (((EntityHuman) iterator.next()).v()) {
                iterator.remove();
            }
        }
        if (this.random.nextInt(2) == 0 && !this.world.players.isEmpty()) {
            if (this.random.nextInt(10) <= 9 && this.getPlayerOwner() != null) {
                this.bA = ((CraftPlayer) this.getPlayerOwner()).getHandle();
            } else {
                this.bA = (Entity) this.world.players.get(this.random.nextInt(this.world.players.size()));
            }
        } else {
            boolean flag;
            do {
                this.a = 0.0D;
                this.b = (70.0F + this.random.nextFloat() * 50.0F);
                this.c = 0.0D;
                this.a += this.random.nextFloat() * 120.0F - 60.0F;
                this.c += this.random.nextFloat() * 120.0F - 60.0F;
                double d0 = this.locX - this.a;
                double d1 = this.locY - this.b;
                double d2 = this.locZ - this.c;

                flag = d0 * d0 + d1 * d1 + d2 * d2 > 100.0D;
            } while (!flag);
            this.bA = null;
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

        this.a = this.locX + (double) (f2 * 5.0F) + (double) ((this.random.nextFloat() - 0.5F) * 2.0F);
        this.b = this.locY + (double) (this.random.nextFloat() * 3.0F) + 1.0D;
        this.c = this.locZ - (double) (f3 * 5.0F) + (double) ((this.random.nextFloat() - 0.5F) * 2.0F);
        this.bA = null;
        if (damageSource.getEntity() instanceof EntityHuman || damageSource.isExplosion()) {
            //this.attack(damageSource.getEntity(), f);
        }

        return true;
    }

    @Override
    protected String getDeathSound() {
        return "";
    }

    @Override
    protected String getIdleSound() {
        return "mob.enderdragon.growl";
    }

    @Override
    public SizeCategory getSizeCategory() {
        return SizeCategory.GIANT;
    }
}
