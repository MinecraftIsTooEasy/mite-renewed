package com.github.jeffyjamzhd.renewed.mixins.block;

import net.minecraft.Block;
import net.minecraft.BlockBreakInfo;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBreakInfo.class)
public class BlockBreakInfoMixin {
    @Shadow public Block block;
    @Shadow public World world;
    @Shadow public int x;
    @Shadow public int y;
    @Shadow public int z;
    @Shadow private int metadata;

    @Inject(method = "getBlockHardness", at = @At("HEAD"), cancellable = true)
    public void getBlockHardnessExtended(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(this.block.mr$getBlockHardness(this.world, this.x, this.y, this.z, this.metadata));
    }
}
