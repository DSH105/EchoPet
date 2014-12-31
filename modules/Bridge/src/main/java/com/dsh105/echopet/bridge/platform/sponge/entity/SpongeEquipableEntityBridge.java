package com.dsh105.echopet.bridge.platform.sponge.entity;

import com.dsh105.commodus.container.ItemStackContainer;
import com.dsh105.echopet.bridge.entity.EquipableEntityBridge;
import com.google.common.base.Optional;
import org.spongepowered.api.entity.ArmorEquipable;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.item.inventory.ItemStack;

public class SpongeEquipableEntityBridge<E extends Living> extends SpongeLivingEntityBridge<E> implements EquipableEntityBridge {

    @Override
    public void setWeapon(ItemStackContainer itemStack) {
        ItemStack stack = null;
        if (itemStack != null) {
            stack = itemStack.asSponge();
        }
        if (getSpongeEntity() instanceof ArmorEquipable) {
            ((ArmorEquipable) getSpongeEntity()).setItemInHand(stack);
        }
    }

    @Override
    public ItemStackContainer getWeapon() {
        if (getSpongeEntity() instanceof ArmorEquipable) {
            Optional<ItemStack> weapon = ((ArmorEquipable) getSpongeEntity()).getItemInHand();
            if (weapon.isPresent()) {
                return ItemStackContainer.of(weapon.get());
            }
        }
        return null;
    }
}