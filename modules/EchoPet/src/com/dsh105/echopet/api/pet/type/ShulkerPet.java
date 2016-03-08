package com.dsh105.echopet.api.pet.type;

import org.bukkit.entity.Player;

import com.dsh105.echopet.api.pet.Pet;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.pet.IShulkerPet;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Mar 7, 2016
 */
@EntityPetType(petType = PetType.SHULKER)
public class ShulkerPet extends Pet implements IShulkerPet{

	public ShulkerPet(Player owner){
		super(owner);
	}
}
