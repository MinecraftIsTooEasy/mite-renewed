package com.github.jeffyjamzhd.renewed.api;

import com.github.jeffyjamzhd.renewed.api.recipe.FurnaceEntry;
import net.minecraft.ItemStack;

import java.util.List;

public interface IFurnaceRecipes {
    default List<FurnaceEntry> mr$getComplexEntries() {
        return null;
    }
    default void mr$addSmeltingComplexEntry(ItemStack input, ItemStack output) {}
    default FurnaceEntry mr$getComplexEntry(ItemStack input, boolean ignoreInputCount) {
        return null;
    }
}
