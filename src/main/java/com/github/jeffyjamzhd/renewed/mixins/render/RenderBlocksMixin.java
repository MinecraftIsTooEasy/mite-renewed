package com.github.jeffyjamzhd.renewed.mixins.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderBlocks.class)
public abstract class RenderBlocksMixin {
    @Shadow public abstract Icon getBlockIcon(Block par1Block, IBlockAccess par2IBlockAccess, int par3, int par4, int par5, int par6);

    @Shadow public IBlockAccess blockAccess;
    @Shadow private double[] x;
    @Shadow private double[] y;
    @Shadow private double[] z;
    @Shadow private double[] u;
    @Shadow private double[] v;
    @Shadow private Icon overrideBlockTexture;

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
