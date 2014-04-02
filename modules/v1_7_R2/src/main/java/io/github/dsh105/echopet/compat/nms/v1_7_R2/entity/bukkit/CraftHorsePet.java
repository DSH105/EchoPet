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

package io.github.dsh105.echopet.compat.nms.v1_7_R2.entity.bukkit;

import io.github.dsh105.echopet.compat.api.entity.EntityPetType;
import io.github.dsh105.echopet.compat.api.entity.IPet;
import io.github.dsh105.echopet.compat.api.entity.PetType;
import io.github.dsh105.echopet.compat.api.entity.type.pet.IHorsePet;
import io.github.dsh105.echopet.compat.nms.v1_7_R2.entity.EntityPet;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.HorseInventory;

@EntityPetType(petType = PetType.HORSE)
public class CraftHorsePet extends CraftAgeablePet implements Horse {

    public CraftHorsePet(EntityPet entity) {
        super(entity);
    }


    @Override
    public Variant getVariant() {
        IPet p = this.getPet();
        if (p instanceof IHorsePet) {
            return ((IHorsePet) p).getHorseType().getBukkitVariant();
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
        IPet p = this.getPet();
        if (p instanceof IHorsePet) {
            return ((IHorsePet) p).getVariant().getBukkitColour();
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
        IPet p = this.getPet();
        if (p instanceof IHorsePet) {
            return ((IHorsePet) p).getMarking().getBukkitStyle();
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
        IPet p = this.getPet();
        if (p instanceof IHorsePet) {
            return ((IHorsePet) p).isChested();
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