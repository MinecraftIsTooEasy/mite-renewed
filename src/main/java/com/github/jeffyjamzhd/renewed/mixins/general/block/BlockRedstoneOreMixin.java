package com.github.jeffyjamzhd.renewed.mixins.block;

import net.minecraft.Block;
import net.minecraft.BlockConstants;
import net.minecraft.BlockRedstoneOre;
import net.minecraft.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockRedstoneOre.class)
abstract class BlockRedstoneOreMixin extends Block {
    protected BlockRedstoneOreMixin(int par1, Material par2Material, BlockConstants constants) {
        super(par1, par2Material, constants);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setStackSize(int par1, boolean par2, CallbackInfo ci) {
        this.setMaxStackSize(8);
    }
}
