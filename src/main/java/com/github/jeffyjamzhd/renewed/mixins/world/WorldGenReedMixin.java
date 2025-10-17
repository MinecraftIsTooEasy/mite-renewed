package com.github.jeffyjamzhd.renewed.mixins.world;

import com.github.jeffyjamzhd.renewed.registry.RenewedBlocks;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.Block;
import net.minecraft.World;
import net.minecraft.WorldGenReed;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldGenReed.class)
public class WorldGenReedMixin {
    @ModifyArg(method = "generate", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/World;setBlock(IIIIII)Z"),
            index = 3)
    private int setBlockID(
            int id) {
        return RenewedBlocks.sugarCane.blockID;
    }

    @Redirect(method = "generate", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/Block;canOccurAt(Lnet/minecraft/World;IIII)Z"))
    private boolean setBlockIDs(
            Block instance, World world, int x, int y, int z, int metadata) {
        return RenewedBlocks.sugarCane.canOccurAt(world, x, y, z, 0);
    }

}
