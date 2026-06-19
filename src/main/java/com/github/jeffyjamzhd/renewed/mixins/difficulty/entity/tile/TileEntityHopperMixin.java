package com.github.jeffyjamzhd.renewed.mixins.difficulty.entity.tile;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.Hopper;
import net.minecraft.TileEntityHopper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TileEntityHopper.class)
public class TileEntityHopperMixin {
    @ModifyExpressionValue(method = "insertStackFromInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/ItemStack;getExperienceReward(I)I"))
    private static int modifyXPReward(int original, @Local(argsOnly = true) Hopper hopper) {
        Difficulty difficulty = Difficulty.getFromWorld(hopper.getWorldObj()).orElseThrow();
        float factor = difficulty.getParamValue(RenewedDifficulties.SMELTING_EXPERIENCE_FACTOR);

        return Math.round(original * factor);
    }
}
