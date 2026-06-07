package com.github.jeffyjamzhd.renewed.mixins.general.render;

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

    @Shadow
    public abstract void setRenderBoundsForStandardFormBlock();

    @Shadow
    public abstract void renderFaceYNeg(Block par1Block, double par2, double par4, double par6, Icon par8Icon);

    @Shadow
    public abstract void renderFaceYPos(Block par1Block, double par2, double par4, double par6, Icon par8Icon);

    @Shadow
    public abstract void renderFaceZNeg(Block par1Block, double par2, double par4, double par6, Icon par8Icon);

    @Shadow
    public abstract void renderFaceZPos(Block par1Block, double par2, double par4, double par6, Icon par8Icon);

    @Shadow
    public abstract void renderFaceXNeg(Block par1Block, double par2, double par4, double par6, Icon par8Icon);

    @Shadow
    public abstract void renderFaceXPos(Block par1Block, double par2, double par4, double par6, Icon par8Icon);

    @Shadow
    public abstract Icon getBlockIconFromSideAndMetadata(Block par1Block, int par2, int par3);

    @Shadow
    public abstract void setRenderBounds(double par1, double par3, double par5, double par7, double par9, double par11);

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
            mr$drawCrossedSquares(block, metadata, icon, dX, dY, dZ, 1.0F);
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "renderBlockByRenderType", at = @At(value = "HEAD"), cancellable = true)
    private void renderCrateBlock(Block block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if (block instanceof BlockCrate crate) {
            if (this.overrideBlockTexture != null) {
                // Rendering damage overlay, so just render the normal block bounds here
                this.setRenderBoundsForStandardFormBlock();
                this.renderStandardBlock(crate, x, y, z);
                cir.setReturnValue(true);
                return;
            }

            // Crate inside
            this.setRenderBounds(1F / 16F, 1F / 16F, 1F / 16F, 15F / 16F, 15F / 16F, 15F / 16F);
            this.renderStandardBlock(crate, x, y, z);

            Tessellator tessellator = Tessellator.instance;
            int brightness = crate.getMixedBrightnessForBlock(this.blockAccess, x, y, z);
            int color = crate.colorMultiplier(this.blockAccess, x, y, z);
            float r = (float)(color >> 16 & 255) / 255.0F;
            float g = (float)(color >> 8 & 255) / 255.0F;
            float b = (float)(color & 255) / 255.0F;
            Icon icon = crate.crateFrame;

            tessellator.setBrightness(brightness);
            tessellator.setColorOpaque_F(r, g, b);

            // Crate frame inner faces
            this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 1.0D);
            this.renderFaceYPos(crate, x, y, z, icon);
            this.setRenderBounds(0.0D, 1.0D, 0.0D, 1.0D, 1.0D, 1.0D);
            this.renderFaceYNeg(crate, x, y, z, icon);
            this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0D);
            this.renderFaceZPos(crate, x, y, z, icon);
            this.setRenderBounds(0.0D, 0.0D, 1.0D, 1.0D, 1.0D, 1.0D);
            this.renderFaceZNeg(crate, x, y, z, icon);
            this.setRenderBounds(0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D);
            this.renderFaceXPos(crate, x, y, z, icon);
            this.setRenderBounds(1.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
            this.renderFaceXNeg(crate, x, y, z, icon);

            // Crate frame outer faces
            this.setOverrideBlockTexture(crate.crateFrame);
            this.setRenderBoundsForStandardFormBlock();
            this.renderStandardBlock(crate, x, y, z);
            this.clearOverrideBlockTexture();

            cir.setReturnValue(true);
        }
    }

    @Inject(method = "renderBlockAsItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/Block;isAlwaysStandardFormCube()Z", ordinal = 0), cancellable = true)
    private void renderCrateBlockAsItem(Block block, int meta, float par3, CallbackInfo ci) {
        if (block instanceof BlockCrate crate) {
            Tessellator tessellator = Tessellator.instance;
            crate.setBlockBoundsForItemRender(meta);
            this.setRenderBounds(.5F / 16F, .5F / 16F, .5F / 16F, 15.5F / 16F, 15.5F / 16F, 15.5F / 16F);

            GL11.glRotatef(90F, 0F, 1.0F, 0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            
            for (int pass = 0; pass < 2; pass++) {
                if (pass == 1) {
                    this.setRenderBoundsForStandardFormBlock();
                    this.setOverrideBlockTexture(crate.crateFrame);
                }

                tessellator.startDrawingQuads();
                tessellator.setNormal(0F, -1.0F, 0F);
                this.renderFaceYNeg(block, 0F, 0F, 0F, this.getBlockIconFromSideAndMetadata(block, 0, meta));
                tessellator.draw();

                tessellator.startDrawingQuads();
                tessellator.setNormal(0F, 1.0F, 0F);
                this.renderFaceYPos(block, 0F, 0F, 0F, this.getBlockIconFromSideAndMetadata(block, 1, meta));
                tessellator.draw();

                tessellator.startDrawingQuads();
                tessellator.setNormal(0F, 0F, -1.0F);
                this.renderFaceZNeg(block, 0F, 0F, 0F, this.getBlockIconFromSideAndMetadata(block, 2, meta));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0F, 0F, 1.0F);
                this.renderFaceZPos(block, 0F, 0F, 0F, this.getBlockIconFromSideAndMetadata(block, 3, meta));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(-1.0F, 0F, 0F);
                this.renderFaceXNeg(block, 0F, 0F, 0F, this.getBlockIconFromSideAndMetadata(block, 4, meta));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(1.0F, 0F, 0F);
                this.renderFaceXPos(block, 0F, 0F, 0F, this.getBlockIconFromSideAndMetadata(block, 5, meta));
                tessellator.draw();
            }

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
            var10.addVertexWithUV(posX, y + 0.0, var26, minU, maxV);
            var10.addVertexWithUV(var24, y + 0.0, var28, maxU, maxV);
            var10.addVertexWithUV(var24, y + scalar, var28, maxU, minV);
            var10.addVertexWithUV(var24, y + scalar, var28, minU, minV);
            var10.addVertexWithUV(var24, y + 0.0, var28, minU, maxV);
            var10.addVertexWithUV(posX, y + 0.0, var26, maxU, maxV);
            var10.addVertexWithUV(posX, y + scalar, var26, maxU, minV);
            var10.addVertexWithUV(posX, y + scalar, var28, minU, minV);
            var10.addVertexWithUV(posX, y + 0.0, var28, minU, maxV);
            var10.addVertexWithUV(var24, y + 0.0, var26, maxU, maxV);
            var10.addVertexWithUV(var24, y + scalar, var26, maxU, minV);
            var10.addVertexWithUV(var24, y + scalar, var26, minU, minV);
            var10.addVertexWithUV(var24, y + 0.0, var26, minU, maxV);
            var10.addVertexWithUV(posX, y + 0.0, var28, maxU, maxV);
            var10.addVertexWithUV(posX, y + scalar, var28, maxU, minV);
        } else {
            this.x[0] = posX;
            this.y[0] = y + scalar;
            this.z[0] = var26;
            this.u[0] = minU;
            this.v[0] = minV;
            this.x[1] = posX;
            this.y[1] = y;
            this.z[1] = var26;
            this.u[1] = minU;
            this.v[1] = maxV;
            this.x[2] = var24;
            this.y[2] = y;
            this.z[2] = var28;
            this.u[2] = maxU;
            this.v[2] = maxV;
            this.x[3] = var24;
            this.y[3] = y + scalar;
            this.z[3] = var28;
            this.u[3] = maxU;
            this.v[3] = minV;
            var10.add4VerticesWithUV(this.x, this.y, this.z, this.u, this.v);
            this.x[0] = var24;
            this.y[0] = y + scalar;
            this.z[0] = var28;
            this.u[0] = minU;
            this.v[0] = minV;
            this.x[1] = var24;
            this.y[1] = y;
            this.z[1] = var28;
            this.u[1] = minU;
            this.v[1] = maxV;
            this.x[2] = posX;
            this.y[2] = y;
            this.z[2] = var26;
            this.u[2] = maxU;
            this.v[2] = maxV;
            this.x[3] = posX;
            this.y[3] = y + scalar;
            this.z[3] = var26;
            this.u[3] = maxU;
            this.v[3] = minV;
            var10.add4VerticesWithUV(this.x, this.y, this.z, this.u, this.v);
            this.x[0] = posX;
            this.y[0] = y + scalar;
            this.z[0] = var28;
            this.u[0] = minU;
            this.v[0] = minV;
            this.x[1] = posX;
            this.y[1] = y;
            this.z[1] = var28;
            this.u[1] = minU;
            this.v[1] = maxV;
            this.x[2] = var24;
            this.y[2] = y;
            this.z[2] = var26;
            this.u[2] = maxU;
            this.v[2] = maxV;
            this.x[3] = var24;
            this.y[3] = y + scalar;
            this.z[3] = var26;
            this.u[3] = maxU;
            this.v[3] = minV;
            var10.add4VerticesWithUV(this.x, this.y, this.z, this.u, this.v);
            this.x[0] = var24;
            this.y[0] = y + scalar;
            this.z[0] = var26;
            this.u[0] = minU;
            this.v[0] = minV;
            this.x[1] = var24;
            this.y[1] = y;
            this.z[1] = var26;
            this.u[1] = minU;
            this.v[1] = maxV;
            this.x[2] = posX;
            this.y[2] = y;
            this.z[2] = var28;
            this.u[2] = maxU;
            this.v[2] = maxV;
            this.x[3] = posX;
            this.y[3] = y + scalar;
            this.z[3] = var28;
            this.u[3] = maxU;
            this.v[3] = minV;
            var10.add4VerticesWithUV(this.x, this.y, this.z, this.u, this.v);
        }
    }
}
