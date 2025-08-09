package com.github.jeffyjamzhd.renewed.compat.emi;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.compat.emi.recipe.HandpanEMIRecipe;
import com.github.jeffyjamzhd.renewed.item.recipe.HandpanRecipe;
import com.github.jeffyjamzhd.renewed.item.recipe.HandpanRecipeProcessor;
import com.github.jeffyjamzhd.renewed.registry.RenewedItems;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.ItemStack;
import net.minecraft.ResourceLocation;
import shims.java.com.unascribed.retroemi.REMIPlugin;

import static com.github.jeffyjamzhd.renewed.MiTERenewed.RESOURCE_ID;

public class RenewedEMIPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry reg) {
        // Add category
        reg.addCategory(RenewedRecipeCategories.HANDPAN);

        // Add recipes
        for (HandpanRecipe recipe : HandpanRecipeProcessor.getRecipes().values())
            reg.addRecipe(new HandpanEMIRecipe(recipe));
    }

    static {
        RenewedRecipeCategories.HANDPAN = new EmiRecipeCategory(
                new ResourceLocation(RESOURCE_ID + "handpan"),
                EmiStack.of(new ItemStack(RenewedItems.handpan, 1, 2))
        );
    }
}
