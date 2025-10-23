package com.github.jeffyjamzhd.renewed.api;

import net.minecraft.ItemStack;

public interface IItem {
    /**
     * {@code true} if the provided {@link ItemStack} is able to be
     * automatically used
     */
    default boolean mr$isAutoUse(ItemStack stack) {
        return false;
    }

    /**
     * {@code true} if the item can be used in crafting
     * @param stack {@link ItemStack} with this item
     */
    default boolean mr$usableInCrafting(ItemStack stack) {
        return !stack.isItemDamaged();
    }
}
