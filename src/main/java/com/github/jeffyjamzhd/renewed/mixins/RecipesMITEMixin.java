package com.github.jeffyjamzhd.renewed.mixins;

import com.github.jeffyjamzhd.renewed.item.recipe.ShapelessToolRecipe;
import com.github.jeffyjamzhd.renewed.registry.RenewedItems;
import com.github.jeffyjamzhd.renewed.registry.RenewedRecipes;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(RecipesMITE.class)
public class RecipesMITEMixin {
    @Inject(method = "addCraftingRecipes", at = @At("TAIL"))
    private static void addSpecialRecipes(CraftingManager crafting_manager, CallbackInfo ci) {
        RenewedRecipes.registerSpecialRecipes(crafting_manager);
    }
}
