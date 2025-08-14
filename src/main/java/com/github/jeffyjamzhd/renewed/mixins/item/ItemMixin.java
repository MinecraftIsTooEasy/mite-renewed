package com.github.jeffyjamzhd.renewed.mixins.item;

import net.minecraft.Item;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Shadow @Final public int itemID;

    @Inject(method = "getBurnTime", at = @At("HEAD"), cancellable = true)
    private void getBurnTimeRenewed(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (this.itemID == Item.feather.itemID)
            cir.setReturnValue(50);
    }
}
