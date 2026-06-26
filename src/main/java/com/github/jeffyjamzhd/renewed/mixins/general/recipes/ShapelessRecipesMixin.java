package com.github.jeffyjamzhd.renewed.mixins.general.recipes;

import com.github.jeffyjamzhd.renewed.item.ItemRenewedBucket;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ShapelessRecipes.class)
public abstract class ShapelessRecipesMixin {
    @Shadow
    public abstract ItemStack[] getComponents();

    @Unique private boolean isBucketRecipe;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void checkForBucket(ItemStack recipe_output, List<?> recipe_items, boolean unused, CallbackInfo ci) {
        boolean hasBucketIngredient = false;
        boolean hasBucketOutput;

        for (Object o : recipe_items) {
            ItemStack stack = (ItemStack) o;
            if (isValidBucket(stack.getItem())) {
                hasBucketIngredient = true;
                break;
            }
        }

        hasBucketOutput = isValidBucket(recipe_output.getItem());
        this.isBucketRecipe = hasBucketIngredient && hasBucketOutput;
    }

    @ModifyReturnValue(method = "getCraftingResult", at = @At("RETURN"))
    private CraftingResult setDamageOnBucketOutput(CraftingResult original,
                                                   @Local(argsOnly = true) InventoryCrafting crafting) {
        if (!this.isBucketRecipe) {
            return original;
        }

        for (ItemStack stack : crafting.getInventory()) {
            if (stack == null) continue;
            if (!(stack.getItem() instanceof ItemRenewedBucket)) continue;

            original.item_stack.setItemDamage(stack.getItemDamage());
            break;
        }

        return original;
    }

    @Unique
    private boolean isValidBucket(Item item) {
        return item instanceof ItemRenewedBucket || item instanceof ItemBucketMilk;
    }
}
