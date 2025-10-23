package com.github.jeffyjamzhd.renewed.mixins.item;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.ItemCoal;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemCoal.class)
public class ItemCoalMixin {
    @ModifyReturnValue(method = "getBurnTime", at = @At("RETURN"))
    private int getBurnTime(int original, @Local(argsOnly = true) ItemStack stack) {
        if (stack.getItemSubtype() == 1) {
            return 800;
        }
        return original;
    }

    @ModifyReturnValue(method = "getHeatLevel", at = @At(value = "RETURN", ordinal = 0))
    private int getHeatLevel(int original) {
        return Math.max(2, original);
    }
}
