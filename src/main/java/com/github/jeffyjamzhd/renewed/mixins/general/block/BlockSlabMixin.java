package com.github.jeffyjamzhd.renewed.mixins.block;

import net.minecraft.Block;
import net.minecraft.BlockConstants;
import net.minecraft.BlockSlab;
import net.minecraft.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockSlab.class)
abstract class BlockSlabMixin extends Block {
    protected BlockSlabMixin(int par1, Material par2Material, BlockConstants constants) {
        super(par1, par2Material, constants);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setStackSizeIfWood(int id, Material material, CallbackInfo ci) {
        if (material == Material.wood) {
            this.setMaxStackSize(16);
        }
    }
}
