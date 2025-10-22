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
     * Gets the max stack size
     */
    default int mr$getMaxStackSize() {
        return 1;
    }
}
