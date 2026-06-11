package com.github.jeffyjamzhd.renewed.api;

import net.minecraft.InventoryPlayer;
import net.minecraft.Item;

public interface IEntityPlayer {
    default void mr$addFoodValueSubtype(Item item, int subtype) {}

    /**
     * Updates the total count of items within backpacks held
     * by the player
     */
    default void mr$updateBackpackItemCount(InventoryPlayer inventory) {}

    /**
     * Returns the amount of items stored within held backpacks
     */
    default int mr$getBackpackItemCount() {
        return 0;
    }
}
