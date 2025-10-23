package com.github.jeffyjamzhd.renewed.mixins.inventory;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.ContainerRepair;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ContainerRepair.class)
public class ContainerRepairMixin {
    @ModifyExpressionValue(method = "isRepairing", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/ItemStack;areItemStacksEqual(Lnet/minecraft/ItemStack;Lnet/minecraft/ItemStack;ZZZZ)Z",
            ordinal = 0))
    private boolean checkForRepair(boolean original, @Local(ordinal = 0) ItemStack inputStack) {
        return original && inputStack.getItem().isRepairable();
    }
}
