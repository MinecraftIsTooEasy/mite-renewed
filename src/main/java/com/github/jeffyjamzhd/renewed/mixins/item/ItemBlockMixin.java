package com.github.jeffyjamzhd.renewed.mixins.item;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.Block;
import net.minecraft.ItemBlock;
import net.minecraft.ItemStack;
import net.minecraft.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemBlock.class)
public class ItemBlockMixin {
    @Inject(method = "getBurnTime", at = @At(value = "HEAD"), cancellable = true)
    private void addBurnTimeCheck(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (stack.getItemAsBlock().getBlock().blockMaterial == Material.plants)
            cir.setReturnValue(13);
    }
}
