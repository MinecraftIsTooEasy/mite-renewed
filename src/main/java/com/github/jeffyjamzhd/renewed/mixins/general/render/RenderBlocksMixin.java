package com.github.jeffyjamzhd.renewed.mixins.general.render;

import com.github.jeffyjamzhd.renewed.api.IBlock;
import com.github.jeffyjamzhd.renewed.block.BlockCrate;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderBlocks.class)
public abstract class RenderBlocksMixin {
    @Shadow public abstract void clearOverrideBlockTexture();
    @Shadow public IBlockAccess blockAccess;
    @Shadow private Icon overrideBlockTexture;

    @Inject(method = "renderBlockByRenderType", at = @At(value = "HEAD"), cancellable = true)
    private void renderCrateBlock(Block block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if (block.mr$useBlockRenderAPI()) {
            RenderBlocks instance = (RenderBlocks) (Object) this;

            if (this.overrideBlockTexture != null) {
                block.mr$renderBlockBreaking(instance, this.blockAccess, x, y, z);
                cir.setReturnValue(true);
                return;
            }

            if (block.mr$renderBlock(instance, this.blockAccess, x, y, z)) {
                block.mr$renderBlockSecondPass(instance, this.blockAccess, x, y, z);
            }

            cir.setReturnValue(true);
        }
    }

    @Inject(method = "renderBlockAsItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/Block;isAlwaysStandardFormCube()Z", ordinal = 0), cancellable = true)
    private void renderCrateBlockAsItem(Block block, int meta, float color, CallbackInfo ci) {
        if (block.mr$useBlockRenderAPI()) {
            GL11.glRotatef(90F, 0F, 1F, 0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

            RenderBlocks instance = (RenderBlocks) (Object) this;
            block.mr$renderBlockAsItem(instance, meta, color);

            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            this.clearOverrideBlockTexture();
            ci.cancel();
        }
    }
}
