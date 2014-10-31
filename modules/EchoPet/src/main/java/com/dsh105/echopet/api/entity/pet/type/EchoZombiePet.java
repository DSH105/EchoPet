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

package com.dsh105.echopet.api.entity.pet.type;

import com.dsh105.echopet.api.entity.*;
import com.dsh105.echopet.api.entity.entitypet.type.EntityZombiePet;
import com.dsh105.echopet.api.entity.pet.EchoEquipablePet;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

@PetInfo(type = PetType.ZOMBIE, width = 0.6F, height = 1.8F)
public class EchoZombiePet<T extends Zombie, S extends EntityZombiePet> extends EchoEquipablePet<T, S> implements ZombiePet<T, S> {

    public EchoZombiePet(Player owner) {
        super(owner);
        setWeapon(new ItemStack(Material.IRON_SPADE));
    }

    @AttributeHandler(data = PetData.VILLAGER)
    @Override
    public void setVillager(boolean flag) {
        getBukkitEntity().setVillager(flag);
    }

    @AttributeHandler(data = PetData.VILLAGER, getter = true)
    @Override
    public boolean isVillager() {
        return getBukkitEntity().isVillager();
    }

    @AttributeHandler(data = PetData.BABY)
    @Override
    public void setBaby(boolean flag) {
        getBukkitEntity().setBaby(true);
    }

    @AttributeHandler(data = PetData.BABY, getter = true)
    @Override
    public boolean isBaby() {
        return getBukkitEntity().isBaby();
    }

    @Override
    public SizeCategory getSizeCategory() {
        return isBaby() ? SizeCategory.TINY : SizeCategory.REGULAR;
    }

    @Override
    public String getIdleSound() {
        return "mob.zombie.say";
    }

    @Override
    public String getDeathSound() {
        return "mob.zombie.death";
    }

    @Override
    public void makeStepSound() {
        getEntity().makeSound("mob.zombie.step", 0.15F, 1.0F);
    }
}