package com.github.jeffyjamzhd.renewed.mixins.block;

import net.minecraft.BlockConstants;
import net.minecraft.BlockFalling;
import net.minecraft.BlockTNT;
import net.minecraft.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockTNT.class)
abstract class BlockTNTMixin extends BlockFalling {
    public BlockTNTMixin(int par1, Material material, BlockConstants constants) {
        super(par1, material, constants);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setStackSize(int id, CallbackInfo ci) {
        this.setMaxStackSize(8);
    }
}
