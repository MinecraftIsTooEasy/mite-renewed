package com.github.jeffyjamzhd.renewed.mixins.accessor;

import net.minecraft.ItemStack;
import net.minecraft.ShapelessRecipes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ShapelessRecipes.class)
public interface ShapelessRecipesAccessor {
    @Accessor("recipeItems")
    List<ItemStack> getRecipeItems();
}
