package com.github.dsh105.echopet.entity.pet.human;
// May support human pets
// This may require a large amount of testing, especially getting the client to render certain skins

import com.github.dsh105.echopet.entity.pet.EntityPet;
import com.github.dsh105.echopet.entity.pet.Pet;
import com.github.dsh105.echopet.entity.pet.SizeCategory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import net.minecraft.server.v1_6_R2.*;

public class EntityHumanPet extends EntityPet {

	private String name;

	public EntityHumanPet(World world) {
		super(world);
	}

	public EntityHumanPet(World world, Pet pet) {
		super(world, pet);
		this.fireProof = true;
		this.a(0.6F, 0.9F);
		this.getNavigation().e(true);
		this.name = this.getPet().getPetName();
	}

	protected void a() {
		super.a();
		this.datawatcher.a(16, Byte.valueOf((byte) 0));
		this.datawatcher.a(17, Float.valueOf(0.0F));
		this.datawatcher.a(18, Integer.valueOf(0));
	}

	protected void a(int i, int j, int k, int l) {
		Location el = this.getLocation();
		Block b = new Location(el.getWorld(), el.getX(), el.getY() - 1, el.getZ()).getBlock();
		if (b.getType() == Material.GRAVEL) {
			this.makeSound("step.gravel", 0.15F, 1.0F);
		}
		else if (b.getType() == Material.LADDER) {
			this.makeSound("step.ladder", 0.15F, 1.0F);
		}
		else if (b.getType() == Material.SAND) {
			this.makeSound("step.sand", 0.15F, 1.0F);
		}
		else if (b.getType() == Material.SNOW) {
			this.makeSound("step.snow", 0.15F, 1.0F);
		}
		else if (b.getType() == Material.STONE) {
			this.makeSound("step.stone", 0.15F, 1.0F);
		}
		else if (b.getType() == Material.WOOD) {
			this.makeSound("step.wood", 0.15F, 1.0F);
		}
		if (b.getType() == Material.WOOL) {
			this.makeSound("step.cloth", 0.15F, 1.0F);
		}
		else {
			this.makeSound("step.grass", 0.15F, 1.0F);
		}
	}

	protected void a(int i, boolean flag) {
		byte b0 = this.datawatcher.getByte(0);

		if (flag) {
			this.datawatcher.watch(0, Byte.valueOf((byte) (b0 | 1 << i)));
		} else {
			this.datawatcher.watch(0, Byte.valueOf((byte) (b0 & ~(1 << i))));
		}
	}

	protected boolean f(int i) {
		return (this.datawatcher.getByte(0) & 1 << i) != 0;
	}

	public boolean isSneaking() {
		return this.f(1);
	}

	public void setSneaking(boolean flag) {
		this.a(1, flag);
	}

	public boolean isSprinting() {
		return this.f(3);
	}

	public void setSprinting(boolean flag) {
		this.a(3, flag);
	}

	public void setInvisible(boolean flag) {
		this.a(5, flag);
	}

	public boolean isInvisible() {
		return this.f(5);
	}

	public String getName() {
		return this.name;
	}

	@Override
	protected String r() {
		return "random.breath";
	}

	@Override
	protected String aO() {
		return "";
	}

	@Override
	public SizeCategory getSizeCategory() {
		return SizeCategory.REGULAR;
	}
}