package com.github.jeffyjamzhd.renewed.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.ItemStack;
import net.minecraft.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerControllerMP.class)
public class PlayerControllerMPMixin {
    @ModifyReturnValue(method = "isItemStackEligibleForAUM", at = @At("RETURN"))
    public boolean aumExtraCondition(
            boolean original, @Local(argsOnly = true) ItemStack stack) {
        return original || (stack != null && stack.getItem().mr$isAutoUse(stack));
    }
}
