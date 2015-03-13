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
import net.minecraft.server.v1_8_R2.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.List;

@EntitySize(width = 16.0F, height = 8.0F)
@EntityPetType(petType = PetType.ENDERDRAGON)
public class EntityEnderDragonPet extends EntityNoClipPet implements IComplex, IMonster, IEntityEnderDragonPet {

    // TODO: ugly :(

    private double a;
    private double b;
    private double c;
    private double h;
    private double j;
    public int bj = -1;
    public double[][] bi = new double[64][3];
    private EntityComplexPart[] children;
    private EntityComplexPart head;
    private EntityComplexPart body;
    private EntityComplexPart tail1;
    private EntityComplexPart tail2;
    private EntityComplexPart tail3;
    private EntityComplexPart wing1;
    private EntityComplexPart wing2;
    private float bs;
    private float bt;
    private boolean bu;
    private boolean bv;
    private int bw;
    private Entity by;

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
        int j = this.bj - i * 1 & 63;
        int k = this.bj - i * 1 - 1 & 63;
        double[] adouble = new double[3];
        double d0 = this.bi[j][0];
        double d1 = MathHelper.g(this.bi[k][0] - d0);

        adouble[0] = d0 + d1 * (double) f;
        d0 = this.bi[j][1];
        d1 = this.bi[k][1] - d0;
        adouble[1] = d0 + d1 * (double) f;
        adouble[2] = this.bi[j][2] + (this.bi[k][2] - this.bi[j][2]) * (double) f;
        return adouble;
    }

    @Override
    public void m() {
        float f;
        float f1;

        if (this.passenger != null && (this.passenger instanceof EntityHuman)) {
            EntityHuman human = (EntityHuman) this.passenger;
            if (human.getBukkitEntity() == this.getPlayerOwner().getPlayer()) {
                float forw = ((EntityLiving) this.passenger).aZ;
                float side = ((EntityLiving) this.passenger).ba;

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
            f = MathHelper.cos(this.bt * 3.1415927F * 2.0F);
            f1 = MathHelper.cos(this.bs * 3.1415927F * 2.0F);
            if (f1 <= -0.3F && f >= -0.3F && !this.R()) {
                this.world.a(this.locX, this.locY, this.locZ, "mob.enderdragon.wings", 5.0F, 0.8F + this.random.nextFloat() * 0.3F, false);
            }
        }

        this.bs = this.bt;
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
            if (this.bv) {
                this.bt += f * 0.5F;
            } else {
                this.bt += f;
            }

            this.yaw = MathHelper.g(this.yaw);
            if (this.bj < 0) {
                for (int i = 0; i < this.bi.length; ++i) {
                    this.bi[i][0] = (double) this.yaw;
                    this.bi[i][1] = this.locY;
                }
            }

            if (++this.bj == this.bi.length) {
                this.bj = 0;
            }

            this.bi[this.bj][0] = (double) this.yaw;
            this.bi[this.bj][1] = this.locY;
            double d0;
            double d1;
            double d2;
            double d3;
            float f3;
            float f17;
            float f16;

            if (this.world.isClientSide) {
                if (this.ba > 0) {
                    d0 = this.locX + (this.bb - this.locX) / (double) this.ba;
                    d1 = this.locY + (this.bc - this.locY) / (double) this.ba;
                    d2 = this.locZ + (this.bd - this.locZ) / (double) this.ba;
                    d3 = MathHelper.g(this.be - (double) this.yaw);
                    this.yaw = (float) ((double) this.yaw + d3 / (double) this.ba);
                    this.pitch = (float) ((double) this.pitch + (this.bf - (double) this.pitch) / (double) this.ba);
                    --this.ba;
                    this.setPosition(d0, d1, d2);
                    this.setYawPitch(this.yaw, this.pitch);
                }
            } else {
                d0 = this.a - this.locX;
                d1 = this.b - this.locY;
                d2 = this.c - this.locZ;
                d3 = d0 * d0 + d1 * d1 + d2 * d2;

                double d8;
                double d9;
                double d4;
                if (this.by != null) {
                    this.a = this.by.locX;
                    this.c = this.by.locZ;
                    d8 = this.a - this.locX;
                    d9 = this.c - this.locZ;
                    double d7 = Math.sqrt(d8 * d8 + d9 * d9);
                    d4 = 0.4000000059604645D + d7 / 80.0D - 1.0D;
                    if (d4 > 10.0D) {
                        d4 = 10.0D;
                    }

                    this.b = this.by.getBoundingBox().b + d4;
                } else {
                    this.a += this.random.nextGaussian() * 2.0D;
                    this.c += this.random.nextGaussian() * 2.0D;
                }

                if (this.bu || d3 < 100.0D || d3 > 22500.0D || this.positionChanged || this.E) {
                    this.target();
                }

                d1 /= (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
                f3 = 0.6F;
                d1 = MathHelper.a(d1, (double) (-f3), (double) f3);

                this.motY += d1 * 0.10000000149011612D;
                this.yaw = MathHelper.g(this.yaw);
                d8 = 180.0D - Math.atan2(d0, d2) * 180.0D / 3.1415927410125732D;
                d9 = MathHelper.g(d8 - (double) this.yaw);

                if (d9 > 50.0D) {
                    d9 = 50.0D;
                }

                if (d9 < -50.0D) {
                    d9 = -50.0D;
                }

                Vec3D f13 = (new Vec3D(this.a - this.locX, this.b - this.locY, this.c - this.locZ)).a();
                d4 = (double) (-MathHelper.cos(this.yaw * 3.1415927F / 180.0F));
                Vec3D j = (new Vec3D((double) MathHelper.sin(this.yaw * 3.1415927F / 180.0F), this.motY, d4)).a();
                float entitycomplexpart = ((float) j.b(f13) + 0.5F) / 1.5F;
                if (entitycomplexpart < 0.0F) {
                    entitycomplexpart = 0.0F;
                }

                this.aZ *= 0.8F;
                float adouble2 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 1.0F + 1.0F;
                double d10 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 1.0D + 1.0D;
                if (d10 > 40.0D) {
                    d10 = 40.0D;
                }

                this.aZ = (float) ((double) this.aZ + d9 * (0.699999988079071D / d10 / (double) adouble2));
                this.yaw += this.aZ * 0.1F;
                f16 = (float) (2.0D / (d10 + 1.0D));
                f17 = 0.06F;
                this.a(0.0F, -1.0F, f17 * (entitycomplexpart * f16 + (1.0F - f16)));
                if (this.bv) {
                    this.move(this.motX * 0.800000011920929D, this.motY * 0.800000011920929D, this.motZ * 0.800000011920929D);
                } else {
                    this.move(this.motX, this.motY, this.motZ);
                }

                Vec3D f18 = (new Vec3D(this.motX, this.motY, this.motZ)).a();
                float f8 = ((float) f18.b(j) + 1.0F) / 2.0F;
                f8 = 0.8F + 0.15F * f8;
                this.motX *= (double) f8;
                this.motZ *= (double) f8;
                this.motY *= 0.9100000262260437D;
            }

            this.updateComplexParts();

            //Nope, absolutely no destruction
            /*if (!this.world.isStatic) {
                this.bv = this.a(this.head.getBoundingBox()) | this.a(this.body.getBoundingBox());
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
            this.aG = this.yaw;
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

            this.body.t_();
            this.body.setPositionRotation(this.locX + (double) (f11 * 0.5F), this.locY, this.locZ - (double) (f12 * 0.5F), 0.0F, 0.0F);
            this.wing1.t_();
            this.wing1.setPositionRotation(this.locX + (double) (f12 * 4.5F), this.locY + 2.0D, this.locZ + (double) (f11 * 4.5F), 0.0F, 0.0F);
            this.wing2.t_();
            this.wing2.setPositionRotation(this.locX - (double) (f12 * 4.5F), this.locY + 2.0D, this.locZ - (double) (f11 * 4.5F), 0.0F, 0.0F);

		/*if (!this.world.isStatic && this.hurtTicks == 0) {
                PetGoalAttack attackGoal = (PetGoalAttack) this.petGoalSelector.getGoal(PetGoalAttack.class);
				if (attackGoal != null && attackGoal.isActive) {
					this.launchEntities(this.world.getEntities(this, this.wing1.getBoundingBox().grow(4.0D, 2.0D, 4.0D).d(0.0D, -2.0D, 0.0D)));
					this.launchEntities(this.world.getEntities(this, this.wing2.getBoundingBox().grow(4.0D, 2.0D, 4.0D).d(0.0D, -2.0D, 0.0D)));
					this.damageEntities(this.world.getEntities(this, this.head.getBoundingBox().grow(1.0D, 1.0D, 1.0D)));
				}
		}*/

            double[] adouble = this.b(5, 1.0F);
            double[] adouble1 = this.b(0, 1.0F);

            float f3 = MathHelper.sin(this.yaw * 3.1415927F / 180.0F - this.aZ * 0.01F);
            float f13 = MathHelper.cos(this.yaw * 3.1415927F / 180.0F - this.aZ * 0.01F);

            this.head.t_();
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

                entitycomplexpart.t_();
                entitycomplexpart.setPositionRotation(this.locX - (double) ((f11 * f17 + f15 * f18) * f2), this.locY + (adouble2[1] - adouble[1]) * 1.0D - (double) ((f18 + f17) * f9) + 1.5D, this.locZ + (double) ((f12 * f17 + f16 * f18) * f2), 0.0F, 0.0F);
            }
        }
    }

    //a(List)
    private void launchEntities(List list) {
        double d0 = (this.body.getBoundingBox().a + this.body.getBoundingBox().d) / 2.0D;
        double d1 = (this.body.getBoundingBox().c + this.body.getBoundingBox().f) / 2.0D;
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

    //b(List)
    private void damageEntities(List list) {
        for (int i = 0; i < list.size(); ++i) {
            Entity entity = (Entity) list.get(i);

            if (entity instanceof EntityLiving) {
                this.attack(entity);
            }
        }
    }

    //bO() EntityEnderDragon
    protected void target() {
        this.bu = false;
        if (this.random.nextInt(2) == 0 && !this.world.players.isEmpty()) {
            if (this.random.nextInt(50) <= 40 && this.getPlayerOwner() != null) {
                this.by = ((CraftPlayer) this.getPlayerOwner()).getHandle();
            } else {
                this.by = (Entity) this.world.players.get(this.random.nextInt(this.world.players.size()));
            }
        } else {
            boolean flag;

            do {
                this.h = 0.0D;
                this.b = (double) (70.0F + this.random.nextFloat() * 50.0F);
                this.j = 0.0D;
                this.h += (double) (this.random.nextFloat() * 120.0F - 60.0F);
                this.j += (double) (this.random.nextFloat() * 120.0F - 60.0F);
                double d0 = this.locX - this.h;
                double d1 = this.locY - this.b;
                double d2 = this.locZ - this.j;

                flag = d0 * d0 + d1 * d1 + d2 * d2 > 100.0D;
            } while (!flag);

            this.by = null;
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
        this.b = this.locY + (double) (this.random.nextFloat() * 3.0F) + 1.0D;
        this.j = this.locZ - (double) (f3 * 5.0F) + (double) ((this.random.nextFloat() - 0.5F) * 2.0F);
        this.by = null;
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
