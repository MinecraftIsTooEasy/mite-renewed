package com.github.jeffyjamzhd.renewed.mixins.render;

import com.github.jeffyjamzhd.renewed.item.ItemHandpan;
import com.github.jeffyjamzhd.renewed.item.ItemPolearm;
import com.github.jeffyjamzhd.renewed.render.ItemRenderHandpan;
import com.github.jeffyjamzhd.renewed.render.ItemRenderPolearm;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Shadow private ItemStack itemToRender;
    @Shadow private Minecraft mc;
    @Shadow @Final private static ResourceLocation RES_ITEM_GLINT;

    @Inject(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V", ordinal = 3), cancellable = true)
    private void renderInjectFirstPerson(
            float par1,
            CallbackInfo ci,
            @Local(name = "var2") float equipTime,
            @Local(name = "var3") EntityClientPlayerMP player,
            @Local(name = "var4") float var4
    ) {
        // Check for polearm
        if (itemToRender.getItem() instanceof ItemPolearm) {
            // Begin render
            GL11.glPushMatrix();

            ItemRenderPolearm.renderPolearm(mc.entityRenderer.itemRenderer, player, itemToRender, par1, equipTime);

            // End render
            GL11.glPopMatrix();


            GL11.glDisable(32826);
            RenderHelper.disableStandardItemLighting();
            ci.cancel();
        }

        // Check for handpan
        if (itemToRender.getItem() instanceof ItemHandpan) {
            // Begin render
            GL11.glPushMatrix();

            ItemRenderHandpan.renderHandpan(par1, itemToRender, player, equipTime, var4);

            // End render
            GL11.glPopMatrix();

            GL11.glDisable(32826);
            RenderHelper.disableStandardItemLighting();
            ci.cancel();
        }
    }

//    @ModifyArg(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityLivingBase;getItemIcon(Lnet/minecraft/ItemStack;I)Lnet/minecraft/Icon;", ordinal = 0), index = 1)
//    private int getIconSpecial(int renderLayer, @Local(name = "par2ItemStack") ItemStack stack) {
//        if (stack.getItem() instanceof ItemPolearm)
//            return 2;
//        return renderLayer;
//    }


    @Inject(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/ItemStack;hasEffect()Z"))
    private void renderEffectIfSpecial(
            EntityLivingBase entity,
            ItemStack stack,
            int layer,
            CallbackInfo ci,
            @Local(name = "var4") TextureManager var4,
            @Local(name = "var6") Tessellator var6
    ) {
        if (stack.getItem() instanceof ItemPolearm && stack.hasEffect() && layer == 2) {
            GL11.glDepthFunc(514);
            GL11.glDisable(2896);
            var4.bindTexture(RES_ITEM_GLINT);
            GL11.glEnable(3042);
            GL11.glBlendFunc(768, 1);
            float var14 = 0.76F;
            GL11.glColor4f(0.5F * var14, 0.25F * var14, 0.8F * var14, 1.0F);
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            float var15 = 0.125F;
            GL11.glScalef(var15, var15, var15);
            float var16 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
            GL11.glTranslatef(var16, 0.0F, 0.0F);
            GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
            ItemRenderer.renderItemIn2D(var6, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(var15, var15, var15);
            var16 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
            GL11.glTranslatef(-var16, 0.0F, 0.0F);
            GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
            ItemRenderer.renderItemIn2D(var6, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
            GL11.glDisable(3042);
            GL11.glEnable(2896);
            GL11.glDepthFunc(515);
        }

    }
}
