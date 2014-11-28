package com.dsh105.echopet.bridge.platform.bukkit.entity;

import com.dsh105.commodus.Affirm;
import com.dsh105.echopet.bridge.entity.AgeableEntityBridge;
import com.dsh105.echopet.bridge.entity.EquipableEntityBridge;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class BukkitEquipableEntityBridge<E extends LivingEntity> extends BukkitLivingEntityBridge<E> implements EquipableEntityBridge {

    @Override
    public void setWeapon(Object itemStack) {
        Affirm.checkInstanceOf(ItemStack.class, itemStack);
        getBukkitEntity().getEquipment().setItemInHand((ItemStack) itemStack);
    }

    @Override
    public int getWeaponItemId() {
        return getBukkitEntity().getEquipment().getItemInHand().getType().getId();
    }
}