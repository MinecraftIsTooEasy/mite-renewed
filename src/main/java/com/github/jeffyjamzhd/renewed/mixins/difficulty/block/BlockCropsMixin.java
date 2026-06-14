package com.github.jeffyjamzhd.renewed.mixins.difficulty.block;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.BlockCrops;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BlockCrops.class)
public class BlockCropsMixin {
    @ModifyArg(method = "updateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/BlockCrops;setBlighted(IZ)I"), index = 1)
    private boolean preventBlightingIfDisabled(boolean blighted, @Local(argsOnly = true) World world) {
        Difficulty difficulty = world.mr$getDifficulty();
        if (difficulty == null) {
            return blighted;
        }

        int sickness = difficulty.getParamValue(RenewedDifficulties.CROP_SICKNESS_BEHAVIOR);
        return sickness != RenewedDifficulties.CROP_SICKNESS_DISABLED && blighted;
    }
}
