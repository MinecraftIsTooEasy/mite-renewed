package com.github.jeffyjamzhd.renewed.api;

import net.minecraft.Item;

public interface IEntityPlayer {
    default void mr$addFoodValueSubtype(Item item, int subtype) {}
}
