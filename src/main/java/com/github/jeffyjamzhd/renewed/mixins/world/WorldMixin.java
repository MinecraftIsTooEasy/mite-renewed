package com.github.jeffyjamzhd.renewed.mixins.world;

import net.minecraft.Block;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldMixin {
    @Shadow public abstract Block getBlock(int x, int y, int z);

    @Shadow public abstract int getBlockMetadata(int x, int y, int z);

    @Inject(method = "getBlockHardness", at = @At("HEAD"), cancellable = true)
    public void getBlockHardness(int x, int y, int z,
                                 CallbackInfoReturnable<Float> cir) {
        Block block = this.getBlock(x, y, z);
        int metadata = this.getBlockMetadata(x, y, z);
        cir.setReturnValue(block.mr$getBlockHardness((World) (Object) this, x, y, z, metadata));
    }
}
