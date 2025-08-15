package com.github.jeffyjamzhd.renewed.mixins.recipes;

import com.github.jeffyjamzhd.renewed.registry.RenewedItems;
import com.github.jeffyjamzhd.renewed.registry.RenewedRecipes;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipesMITE.class)
public class RecipesMITEMixin {
    @Inject(method = "addCraftingRecipes", at = @At("TAIL"))
    private static void addSpecialRecipes(CraftingManager crafting_manager, CallbackInfo ci) {
        RenewedRecipes.registerSpecialRecipes(crafting_manager);
    }

    /**
     * Modifies sinew recipe count (4 -> 1)
     */
    @ModifyArg(method = "addCraftingRecipes", at = @At(value = "INVOKE", target = "Lnet/minecraft/CraftingManager;addRecipe(Lnet/minecraft/ItemStack;[Ljava/lang/Object;)Lnet/minecraft/ShapedRecipes;", ordinal = 13), index = 0)
    private static ItemStack modifySinewCount(ItemStack stack) {
        return stack.setStackSize(1);
    }

    /**
     * Modifies sinew difficulty (50F -> 625F)
     */
    @ModifyArg(method = "addCraftingRecipes", at = @At(value = "INVOKE", target = "Lnet/minecraft/ShapedRecipes;setDifficulty(F)Lnet/minecraft/IRecipe;", ordinal = 0), index = 0)
    private static float modifySinewDifficulty(float difficulty) {
        return 625F;
    }

    /**
     * Modifies beef stew to accept subtypes
     */
    @ModifyArg(method = "addCraftingRecipes", at = @At(value = "INVOKE", target = "Lnet/minecraft/CraftingManager;addShapelessRecipe(Lnet/minecraft/ItemStack;[Ljava/lang/Object;)Lnet/minecraft/ShapelessRecipes;", ordinal = 11), index = 1)
    private static Object[] modBeefStew(Object[] original) {
        original[0] = new ItemStack(RenewedItems.cooked_beef, 1, Short.MAX_VALUE);
        return original;
    }

    /**
     * Modifies chicken soup to accept subtypes
     */
    @ModifyArg(method = "addCraftingRecipes", at = @At(value = "INVOKE", target = "Lnet/minecraft/CraftingManager;addShapelessRecipe(Lnet/minecraft/ItemStack;[Ljava/lang/Object;)Lnet/minecraft/ShapelessRecipes;", ordinal = 12), index = 1)
    private static Object[] modChickenSoup(Object[] original) {
        original[0] = new ItemStack(RenewedItems.cooked_poultry, 1, Short.MAX_VALUE);
        return original;
    }
}
