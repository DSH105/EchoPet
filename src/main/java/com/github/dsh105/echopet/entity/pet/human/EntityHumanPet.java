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
import org.bukkit.craftbukkit.v1_6_R2.*;

public class EntityHumanPet extends EntityPet {
	
	public EntityHumanPet(World world, Pet pet) {
		super(world, pet);
		this.fireProof = true;
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