package com.github.jeffyjamzhd.renewed.api;

import net.minecraft.ItemStack;

public interface IInventoryPlayer {
    /**
     * Hook for specifically adding items from the ground
     * to the player's inventory, for backpack filters to pick up on
     */
    default boolean mr$addStackFromWorld(ItemStack stack) {
        return false;
    }
}
