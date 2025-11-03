package com.github.jeffyjamzhd.renewed.mixins.block;

import net.minecraft.Block;
import net.minecraft.BlockChest;
import net.minecraft.BlockMounted;
import net.minecraft.EnumFace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockMounted.class)
public class BlockMountedMixin {
    @Inject(method = "canMountToBlock(ILnet/minecraft/Block;ILnet/minecraft/EnumFace;)Z", at = @At("HEAD"), cancellable = true)
    private void mountToChest(int metadata, Block neighbor_block, int neighbor_block_metadata, EnumFace face,
                              CallbackInfoReturnable<Boolean> cir) {
        if (neighbor_block instanceof BlockChest)
            cir.setReturnValue(true);
    }
}
