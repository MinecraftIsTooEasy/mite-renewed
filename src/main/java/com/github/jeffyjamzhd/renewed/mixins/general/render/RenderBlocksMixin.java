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
    @Shadow public abstract Icon getBlockIcon(Block par1Block, IBlockAccess par2IBlockAccess, int par3, int par4, int par5, int par6);
    @Shadow public abstract boolean renderStandardBlock(Block par1Block, int par2, int par3, int par4);
    @Shadow public abstract void setOverrideBlockTexture(Icon par1Icon);
    @Shadow public abstract void clearOverrideBlockTexture();

    @Shadow public IBlockAccess blockAccess;
    @Shadow private double[] x;
    @Shadow private double[] y;
    @Shadow private double[] z;
    @Shadow private double[] u;
    @Shadow private double[] v;
    @Shadow private Icon overrideBlockTexture;

    @Shadow public abstract void setRenderBoundsForStandardFormBlock();
    @Shadow public abstract void renderFaceYNeg(Block par1Block, double par2, double par4, double par6, Icon par8Icon);
    @Shadow public abstract void renderFaceYPos(Block par1Block, double par2, double par4, double par6, Icon par8Icon);
    @Shadow public abstract void renderFaceZNeg(Block par1Block, double par2, double par4, double par6, Icon par8Icon);
    @Shadow public abstract void renderFaceZPos(Block par1Block, double par2, double par4, double par6, Icon par8Icon);
    @Shadow public abstract void renderFaceXNeg(Block par1Block, double par2, double par4, double par6, Icon par8Icon);
    @Shadow public abstract void renderFaceXPos(Block par1Block, double par2, double par4, double par6, Icon par8Icon);
    @Shadow public abstract Icon getBlockIconFromSideAndMetadata(Block par1Block, int par2, int par3);
    @Shadow public abstract void setRenderBounds(double par1, double par3, double par5, double par7, double par9, double par11);

    @Inject(method = "renderCrossedSquares", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/RenderBlocks;drawCrossedSquares(Lnet/minecraft/Block;IDDDF)V"),
            cancellable = true)
    private void renderCrossedSquaresAlternate(
            Block block, int x, int y, int z,
            CallbackInfoReturnable<Boolean> cir,
            @Local(ordinal = 0) double dX,
            @Local(ordinal = 1) double dY,
            @Local(ordinal = 2) double dZ) {
        if (block.mr$useSpecialCrossedRenderer()) {
            Icon icon = getBlockIcon(block, blockAccess, x, y, z, 0);
            int metadata = blockAccess.getBlockMetadata(x, y, z);
            mr$drawCrossedSquares(block, metadata, icon, dX, dY, dZ, 1F);
            cir.setReturnValue(true);
        }
    }

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

    /**
     * Alternative method to RenderBlocks#drawCrossedSquares, allows
     * for an icon to be passed in
     */
    @Unique
    private void mr$drawCrossedSquares(Block block, int metadata, Icon icon,
                                       double x, double y, double z, double scalar) {
        Tessellator var10 = Tessellator.instance;
        icon = this.overrideBlockTexture == null ? icon : this.overrideBlockTexture;

        double minU = icon.getMinU();
        double minV = icon.getMinV();
        double maxU = icon.getMaxU();
        double maxV = icon.getMaxV();
        double var20 = 0.45 * scalar;
        double posX = x + 0.5 - var20;
        double var24 = x + 0.5 + var20;
        double var26 = z + 0.5 - var20;
        double var28 = z + 0.5 + var20;

        if (RenderingScheme.current == 0) {
            var10.addVertexWithUV(posX, y + scalar, var26, minU, minV);
            var10.addVertexWithUV(posX, y + 0, var26, minU, maxV);
            var10.addVertexWithUV(var24, y + 0, var28, maxU, maxV);
            var10.addVertexWithUV(var24, y + scalar, var28, maxU, minV);
            var10.addVertexWithUV(var24, y + scalar, var28, minU, minV);
            var10.addVertexWithUV(var24, y + 0, var28, minU, maxV);
            var10.addVertexWithUV(posX, y + 0, var26, maxU, maxV);
            var10.addVertexWithUV(posX, y + scalar, var26, maxU, minV);
            var10.addVertexWithUV(posX, y + scalar, var28, minU, minV);
            var10.addVertexWithUV(posX, y + 0, var28, minU, maxV);
            var10.addVertexWithUV(var24, y + 0, var26, maxU, maxV);
            var10.addVertexWithUV(var24, y + scalar, var26, maxU, minV);
            var10.addVertexWithUV(var24, y + scalar, var26, minU, minV);
            var10.addVertexWithUV(var24, y + 0, var26, minU, maxV);
            var10.addVertexWithUV(posX, y + 0, var28, maxU, maxV);
            var10.addVertexWithUV(posX, y + scalar, var28, maxU, minV);
        }
    }
}
