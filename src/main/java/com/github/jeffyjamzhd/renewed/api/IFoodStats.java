package com.github.jeffyjamzhd.renewed.api;

import net.minecraft.Item;

public interface IFoodStats {
    default void mr$addFoodValueSubtype(Item item, int subtype) {}
}
