package com.github.jeffyjamzhd.renewed.api;

import net.minecraft.EntityPlayer;
import net.minecraft.MITEContainerCrafting;

public interface ISlotCrafting {
    /**
     * Wrapper for SlotCrafing#setInitialItemStack
     * @param player Player entity
     * @param container Crafting container
     */
    default void mr$setInitialItemStack(EntityPlayer player, MITEContainerCrafting container) {
    }
}
