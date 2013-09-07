package com.github.dsh105.echopet.entity.pet;

import net.minecraft.server.v1_6_R2.World;

/**
 * Project by DSH105
 */

public abstract class EntityNoClipPet extends EntityPet {

	public EntityNoClipPet(World world, Pet pet) {
		super(world, pet);
	}

	protected EntityNoClipPet(World world) {
		super(world);
	}

	public void noClip(boolean b) {
		this.Z = b;
	}
}