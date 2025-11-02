package com.github.jeffyjamzhd.renewed.mixins.block;

import net.minecraft.Block;
import net.minecraft.BlockBookshelf;
import net.minecraft.BlockConstants;
import net.minecraft.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBookshelf.class)
abstract class BlockBookshelfMixin extends Block {
    protected BlockBookshelfMixin(int par1, Material par2Material, BlockConstants constants) {
        super(par1, par2Material, constants);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setStackSize(int id, CallbackInfo ci) {
        this.setMaxStackSize(8);
    }
}
