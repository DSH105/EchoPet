package com.github.dsh105.echopet.entity.pet.zombie;

import net.minecraft.server.v1_6_R2.Item;
import net.minecraft.server.v1_6_R2.ItemStack;
import net.minecraft.server.v1_6_R2.World;

import org.bukkit.scheduler.BukkitRunnable;

import com.github.dsh105.echopet.EchoPet;
import com.github.dsh105.echopet.entity.pet.EntityPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.SizeCategory;

public class EntityZombiePet extends EntityPet {

	public EntityZombiePet(World world) {
		super(world);
	}

	public EntityZombiePet(World world, Pet pet) {
		super(world, pet);
		this.a(0.6F, 0.9F);
		this.fireProof = true;
		new BukkitRunnable() {
			public void run() {
				setEquipment(0, new ItemStack(Item.IRON_SPADE));
			}
		}.runTaskLater(EchoPet.getPluginInstance(), 5L);
	}
	
	public void setBaby(boolean flag) {
		this.datawatcher.watch(12, (byte) (flag ? 1 : 0));
		((ZombiePet) pet).baby = flag;
	}
	
	public void setVillager(boolean flag) {
		this.datawatcher.watch(13, (byte) (flag ? 1 : 0));
		((ZombiePet) pet).villager = flag;
	}

	protected void a() {
		super.a();
		this.datawatcher.a(12, new Byte((byte) 0));
		this.datawatcher.a(13, new Byte((byte) 0));
		//this.datawatcher.a(14, Byte.valueOf((byte) 0));
	}
	
	@Override
	protected String getIdleSound() {
        return "mob.zombie.say";
    }

	protected void a(int i, int j, int k, int l) {
		this.makeSound("mob.zombie.step", 0.15F, 1.0F);
	}
	
	@Override
	protected String getDeathSound() {
		return "mob.zombie.death";
	}
	
	public boolean isBaby() {
		return this.datawatcher.getByte(12) < 0;
	}
	
	@Override
	public SizeCategory getSizeCategory() {
		if (this.isBaby()) {
			return SizeCategory.TINY;
		}
		else {
			return SizeCategory.REGULAR;
		}
	}
}