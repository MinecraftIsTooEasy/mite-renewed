package com.github.jeffyjamzhd.renewed.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.Container;
import net.minecraft.InventoryCrafting;
import net.minecraft.ItemDagger;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InventoryCrafting.class)
public class InventoryCraftingMixin {

    @Shadow private ItemStack[] stackList;

    @Shadow private Container eventHandler;

    @Inject(method = "hasDamagedItem", at = @At(value = "FIELD", target = "Lnet/minecraft/InventoryCrafting;stackList:[Lnet/minecraft/ItemStack;", ordinal = 1), cancellable = true)
    private void circumventDamageCheck(CallbackInfoReturnable<Boolean> cir, @Local(name = "i") int i) {
        // Inside the for loop... get current ItemStack
        ItemStack stack = this.stackList[i];

        // Check if tool exists, and if recipe matches ShapelessToolRecipe
        if (stack != null && stack.getItem() instanceof ItemDagger)
            cir.setReturnValue(false);
    }
}
