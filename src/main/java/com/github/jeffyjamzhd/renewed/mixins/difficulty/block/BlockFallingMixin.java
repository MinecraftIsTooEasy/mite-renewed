package com.github.jeffyjamzhd.renewed.mixins.difficulty.block;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockFalling.class)
public class BlockFallingMixin {
    @ModifyReturnValue(method = "canFallDownTo", at = @At("RETURN"))
    private boolean canFallThroughLeaves(boolean original, @Local Block block, @Local(argsOnly = true) World world) {
        Difficulty difficulty = Difficulty.getFromWorld(world).orElseThrow();
        boolean canPassThrough = difficulty.getParamValue(RenewedDifficulties.NON_SOLID_LEAVES);
        if (block instanceof BlockLeavesBase) {
            return canPassThrough || original;
        }
        return original;
    }
}
