package io.github.dsh105.echopet.entity.type.horse;

import io.github.dsh105.echopet.entity.CraftAgeablePet;
import io.github.dsh105.echopet.entity.EntityPet;
import io.github.dsh105.echopet.entity.EntityPetType;
import io.github.dsh105.echopet.entity.Pet;
import io.github.dsh105.echopet.entity.PetType;
import org.bukkit.craftbukkit.v1_7_R2.CraftServer;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.HorseInventory;

@EntityPetType(petType = PetType.HORSE)
public class CraftHorsePet extends CraftAgeablePet implements Horse {

    public CraftHorsePet(CraftServer server, EntityPet entity) {
        super(server, entity);
    }


    @Override
    public Variant getVariant() {
        Pet p = this.getPet();
        if (p instanceof HorsePet) {
            return ((HorsePet) p).getHorseType().getBukkitVariant();
        }
        return null;
    }

    @Override
    public void setVariant(Variant variant) {
        /*Pet p = this.getPet();
        if (p instanceof HorsePet) {
            ((HorsePet) p).setHorseType(HorseType.getForBukkitVariant(variant));
        }*/
    }

    @Override
    public Color getColor() {
        Pet p = this.getPet();
        if (p instanceof HorsePet) {
            return ((HorsePet) p).getVariant().getBukkitColour();
        }
        return null;
    }

    @Override
    public void setColor(Color color) {
        /*Pet p = this.getPet();
        if (p instanceof HorsePet) {
            ((HorsePet) p).setVariant(HorseVariant.getForBukkitColour(color), ((HorsePet) p).getMarking());
        }*/
    }

    @Override
    public Style getStyle() {
        Pet p = this.getPet();
        if (p instanceof HorsePet) {
            return ((HorsePet) p).getMarking().getBukkitStyle();
        }
        return null;
    }

    @Override
    public void setStyle(Style style) {
        /*Pet p = this.getPet();
        if (p instanceof HorsePet) {
            ((HorsePet) p).setVariant(((HorsePet) p).getVariant(), HorseMarking.getForBukkitStyle(style));
        }*/
    }

    @Override
    public boolean isCarryingChest() {
        Pet p = this.getPet();
        if (p instanceof HorsePet) {
            return ((HorsePet) p).isChested();
        }
        return false;
    }

    @Override
    public void setCarryingChest(boolean b) {
        /*Pet p = this.getPet();
        if (p instanceof HorsePet) {
            ((HorsePet) p).setChested(b);
        }*/
    }

    @Override
    public int getDomestication() {
        return 0; // Doesn't apply to pets
    }

    @Override
    public void setDomestication(int i) {
        // Doesn't apply to pets
    }

    @Override
    public int getMaxDomestication() {
        return 0; // Doesn't apply to pets
    }

    @Override
    public void setMaxDomestication(int i) {
        // Doesn't apply to pets
    }

    @Override
    public double getJumpStrength() {
        return 0; // Doesn't apply to pets
    }

    @Override
    public void setJumpStrength(double v) {
        // Doesn't apply to pets
    }

    @Override
    public HorseInventory getInventory() {
        return null; // Doesn't apply to pets
    }
}