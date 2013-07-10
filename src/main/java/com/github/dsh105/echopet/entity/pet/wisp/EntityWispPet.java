/*package com.github.dsh105.echopet.entity.pet.wisp;

import net.minecraft.server.v1_6_R1.*;
import org.bukkit.craftbukkit.v1_6_R1.*;

import com.github.dsh105.echopet.entity.pet.EntityPet;
import com.github.dsh105.echopet.entity.pet.Pet;

public class EntityWispPet extends EntityPet {

	public int a;
	public int c;
	private EntityHuman targetPlayer;
	private int targetTime;
	
	public EntityWispPet(World world, Pet pet) {
		super(world, pet);
		this.a(0.5F, 0.5F);
		this.fireProof = true;
	}

	@Override
	protected String r() {
		return "";
	}

	public void l_() {
		super.l_();
		if (this.c > 0) {
			--this.c;
		}

		this.lastX = this.locX;
		this.lastY = this.locY;
		this.lastZ = this.locZ;
		this.motY -= 0.029999999329447746D;

		this.i(this.locX, (this.boundingBox.b + this.boundingBox.e) / 2.0D, this.locZ);
		double d0 = 8.0D;

		if (this.targetTime < this.a - 20 + this.id % 100) {
			if (this.targetPlayer == null || this.targetPlayer.e(this) > d0 * d0) {
				this.targetPlayer = this.world.findNearbyPlayer(this, d0);
			}

			this.targetTime = this.a;
		}

		if (this.targetPlayer != null) {
			double d1 = (this.targetPlayer.locX - this.locX) / d0;
			double d2 = (this.targetPlayer.locY + (double) this.targetPlayer.getHeadHeight() - this.locY) / d0;
			double d3 = (this.targetPlayer.locZ - this.locZ) / d0;
			double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
			double d5 = 1.0D - d4;

			if (d5 > 0.0D) {
				d5 *= d5;
				this.motX += d1 / d4 * d5 * 0.1D;
				this.motY += d2 / d4 * d5 * 0.1D;
				this.motZ += d3 / d4 * d5 * 0.1D;
			}
		}

		this.move(this.motX, this.motY, this.motZ);
		float f = 0.98F;

		if (this.onGround) {
			f = 0.58800006F;
			int i = this.world.getTypeId(MathHelper.floor(this.locX), MathHelper.floor(this.boundingBox.b) - 1, MathHelper.floor(this.locZ));

			if (i > 0) {
				f = Block.byId[i].frictionFactor * 0.98F;
			}
		}

		this.motX *= (double) f;
		this.motY *= 0.9800000190734863D;
		this.motZ *= (double) f;
		if (this.onGround) {
			this.motY *= -0.8999999761581421D;
		}

		++this.a;
	}
}*/