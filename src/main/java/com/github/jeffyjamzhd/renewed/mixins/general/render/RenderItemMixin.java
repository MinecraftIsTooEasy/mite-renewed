package com.github.jeffyjamzhd.renewed.mixins.general.render;

import com.github.jeffyjamzhd.renewed.render.RenderItemBeam;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.EntityItem;
import net.minecraft.Render;
import net.minecraft.RenderItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(RenderItem.class)
public abstract class RenderItemMixin extends Render {
    @Inject(method = "doRenderItem", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V", ordinal = 0))
    private void renderLootBeam(EntityItem par1EntityItem, double par2, double par4, double par6, float par8, float par9, CallbackInfo ci, @Local(ordinal = 2) float var11) {
        RenderItemBeam.renderItemLootBeam(par1EntityItem, renderManager.playerViewY, var11, par9);

    }
}
