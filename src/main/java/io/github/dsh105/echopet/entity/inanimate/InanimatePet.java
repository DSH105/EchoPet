package io.github.dsh105.echopet.entity.inanimate;

import io.github.dsh105.echopet.entity.ICraftPet;
import io.github.dsh105.echopet.entity.IEntityPet;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.entity.Player;

public class InanimatePet extends Pet {

    public InanimatePet(Player owner, PetType petType) {
        super(owner, petType);
        this.setName(petType.getDefaultName(owner.getName()));
    }

    @Override
    protected EntityInanimatePet initiatePet() {
        return null;
    }

    /**
     * Gets the {@link io.github.dsh105.echopet.entity.IEntityPet} for this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @return a {@link io.github.dsh105.echopet.entity.IEntityPet} object for this {@link io.github.dsh105.echopet.entity.Pet}
     */
    @Override
    public EntityInanimatePet getEntityPet() {
        return null;
    }

    /**
     * Gets the {@link io.github.dsh105.echopet.entity.ICraftPet} for this {@link io.github.dsh105.echopet.entity.Pet}
     *
     * @return a {@link io.github.dsh105.echopet.entity.ICraftPet} object for this {@link io.github.dsh105.echopet.entity.Pet}
     */
    @Override
    public CraftInanimatePet getCraftPet() {
        return null;
    }

    @Override
    public ICraftPet setCraftPet(ICraftPet craftPet) {
        return null;
    }
}