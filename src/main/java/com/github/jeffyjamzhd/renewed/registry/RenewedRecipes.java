package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.item.recipe.ShapelessToolRecipe;
import com.github.jeffyjamzhd.renewed.registry.RenewedItems;
import net.minecraft.*;
import net.xiaoyu233.fml.reload.event.RecipeRegistryEvent;

import java.util.Arrays;

public class RenewedRecipes {
    public static void registerRecipes(RecipeRegistryEvent registry) {
        registerToolRecipes(registry);
    }

    public static void registerToolRecipes(RecipeRegistryEvent registry) {

    }

    public static ShapelessToolRecipe registerToolRecipe(ItemStack output, Item tool, CraftingManager man, Item... input) {
        ShapelessToolRecipe recipe = new ShapelessToolRecipe(output, new ItemStack(tool), Arrays.stream(input).map(ItemStack::new).toList());
        man.getRecipeList().add(recipe);
        return recipe;
    }

    public static void registerSpecialRecipes(CraftingManager manager) {
        // Iterate and add knife -> sharp bone recipes
        for (Item item : Item.itemsList)
            if (item instanceof ItemDagger && item.itemID != RenewedItems.sharp_bone.itemID)
                RenewedRecipes.registerToolRecipe(new ItemStack(RenewedItems.sharp_bone), item, manager, Item.bone).setDamage(80).setDifficulty(1200F);
    }
}
