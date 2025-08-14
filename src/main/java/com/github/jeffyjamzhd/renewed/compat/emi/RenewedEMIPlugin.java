package com.github.jeffyjamzhd.renewed.compat.emi;

import com.github.jeffyjamzhd.renewed.api.IFurnaceRecipes;
import com.github.jeffyjamzhd.renewed.api.recipe.FurnaceEntry;
import com.github.jeffyjamzhd.renewed.compat.emi.recipe.HandpanEMIRecipe;
import com.github.jeffyjamzhd.renewed.item.ItemRenewedFood;
import com.github.jeffyjamzhd.renewed.item.recipe.HandpanRecipe;
import com.github.jeffyjamzhd.renewed.item.recipe.HandpanRecipeProcessor;
import com.github.jeffyjamzhd.renewed.registry.RenewedItems;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.recipe.EmiCookingRecipe;
import moddedmite.emi.recipe.EmiFoodRecipe;
import net.minecraft.*;

import java.util.List;

import static com.github.jeffyjamzhd.renewed.MiTERenewed.RESOURCE_ID;

public class RenewedEMIPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry reg) {
        // Add category
        reg.addCategory(RenewedRecipeCategories.HANDPAN);

        // Add recipes
        for (HandpanRecipe recipe : HandpanRecipeProcessor.getRecipes().values())
            reg.addRecipe(new HandpanEMIRecipe(recipe));

        for (FurnaceEntry entry : ((IFurnaceRecipes)FurnaceRecipes.smelting()).mr$getComplexEntries()) {
            ResourceLocation cookingId = new ResourceLocation(RESOURCE_ID + "furnace/" + entry.hashCode());
            ResourceLocation worldId = new ResourceLocation(RESOURCE_ID + "world/" + entry.hashCode());
            reg.addRecipe(new EmiCookingRecipe(cookingId, entry.input(), entry.output(), VanillaEmiRecipeCategories.SMELTING, 0, entry.output().getExperienceReward()));
            reg.addRecipe(EmiWorldInteractionRecipe.builder()
                    .id(worldId)
                    .leftInput(EmiStack.of(entry.input().splitStack(1)))
                    .rightInput(EmiIngredient.of(List.of(EmiStack.of(new ItemStack(Block.fire)))), true)
                    .output(EmiStack.of(entry.output().splitStack(1)))
                    .build());
        }


        for (Item item : Item.itemsList) {
            // Add special foods
            if (item instanceof ItemRenewedFood) {
                for (int sub = 0; sub < item.getNumSubtypes(); ++sub)
                    reg.addRecipe(new EmiFoodRecipe(new ItemStack(item.itemID, 1, sub)));
            }
        }


    }

    static {
        RenewedRecipeCategories.HANDPAN = new EmiRecipeCategory(
                new ResourceLocation(RESOURCE_ID + "handpan"),
                EmiStack.of(new ItemStack(RenewedItems.handpan, 1, 2))
        );
    }
}
