package com.github.jeffyjamzhd.renewed.mixins.block;

import net.minecraft.BlockBreakable;
import net.minecraft.BlockConstants;
import net.minecraft.BlockGlass;
import net.minecraft.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockGlass.class)
abstract class BlockGlassMixin extends BlockBreakable {
    protected BlockGlassMixin(int par1, String par2Str, Material par3Material, boolean par4, BlockConstants block_constants) {
        super(par1, par2Str, par3Material, par4, block_constants);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setStackSize(int id, Material material, boolean par3, CallbackInfo ci) {
        this.setMaxStackSize(8);
    }
}
