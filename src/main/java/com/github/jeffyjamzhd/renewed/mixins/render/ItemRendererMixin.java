package com.github.jeffyjamzhd.renewed.mixins.render;

import com.github.jeffyjamzhd.renewed.item.ItemHandpan;
import com.github.jeffyjamzhd.renewed.item.ItemPolearm;
import com.github.jeffyjamzhd.renewed.render.ItemRenderHandpan;
import com.github.jeffyjamzhd.renewed.render.ItemRenderPolearm;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Shadow private ItemStack itemToRender;

    @Shadow private Minecraft mc;

    @Inject(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V", ordinal = 3), cancellable = true)
    private void renderInject(
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
}
