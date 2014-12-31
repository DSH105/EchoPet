package com.dsh105.echopet.bridge.platform.bukkit.entity;

import com.dsh105.commodus.Affirm;
import com.dsh105.commodus.container.ItemStackContainer;
import com.dsh105.echopet.bridge.entity.EquipableEntityBridge;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class BukkitEquipableEntityBridge<E extends LivingEntity> extends BukkitLivingEntityBridge<E> implements EquipableEntityBridge {

    @Override
    public void setWeapon(ItemStackContainer itemStack) {
        ItemStack stack = null;
        if (itemStack != null) {
            stack = itemStack.asBukkit();
        }
        getBukkitEntity().getEquipment().setItemInHand(stack);
    }

    @Override
    public ItemStackContainer getWeapon() {
        ItemStack weapon = getBukkitEntity().getEquipment().getItemInHand();
        if (weapon != null) {
            return ItemStackContainer.of(weapon);
        }
        return null;
    }
}