package com.github.dsh105.echopet.entity.pet.human;
// May support human pets
// This may require a large amount of testing, especially getting the client to render certain skins

import com.github.dsh105.echopet.entity.pet.Pet;

import org.bukkit.entity.Player;

import com.github.dsh105.echopet.data.PetType;

public class HumanPet extends Pet {

	public HumanPet(Player owner, PetType petType) {
		super(owner, petType);
	}
}