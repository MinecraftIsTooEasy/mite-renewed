package com.github.jeffyjamzhd.renewed.mixins.item;

import net.minecraft.Item;
import net.minecraft.ItemStack;
import net.minecraft.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void modifyDefinitions(
            CallbackInfo ci) {
    }

    @Inject(method = "getBurnTime", at = @At("HEAD"), cancellable = true)
    private void getBurnTimeRenewed(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (stack.getItem().itemID == Item.feather.itemID)
            cir.setReturnValue(50);
    }
}
