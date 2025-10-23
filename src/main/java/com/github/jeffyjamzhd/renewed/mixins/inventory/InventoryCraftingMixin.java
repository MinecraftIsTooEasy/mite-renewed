package com.github.jeffyjamzhd.renewed.mixins.inventory;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.InventoryCrafting;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InventoryCrafting.class)
abstract class InventoryCraftingMixin {
    @ModifyExpressionValue(method = "hasDamagedItem", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/ItemStack;isItemDamaged()Z",
            ordinal = 0))
    private boolean canCraftOverride(boolean original, @Local ItemStack stack) {
        if (stack == null) {
            return original;
        }
        return original && !stack.getItem().mr$usableInCrafting(stack);
    }
}
