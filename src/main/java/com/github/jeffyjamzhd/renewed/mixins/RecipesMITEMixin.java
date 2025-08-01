package com.github.jeffyjamzhd.renewed.mixins;

import com.github.jeffyjamzhd.renewed.item.recipe.ShapelessToolRecipe;
import com.github.jeffyjamzhd.renewed.registry.RenewedItems;
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
        for (Item item : Item.itemsList) {
            if (item instanceof ItemDagger tool) {
                crafting_manager.getRecipeList().add(new ShapelessToolRecipe(
                        new ItemStack(RenewedItems.sharp_bone),
                        new ItemStack(tool),
                        List.of(new ItemStack(Item.bone))
                ).damage(50));
            }
        }
    }
}
