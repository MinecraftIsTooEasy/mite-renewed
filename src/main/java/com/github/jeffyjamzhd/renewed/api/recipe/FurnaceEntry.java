package com.github.jeffyjamzhd.renewed.api.recipe;

import net.minecraft.ItemStack;

public record FurnaceEntry(ItemStack input, ItemStack output) {
    public int getSubtype() {
        return this.input().getItemSubtype();
    }
}
