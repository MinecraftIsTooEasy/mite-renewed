package com.github.jeffyjamzhd.renewed.mixins.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ModelBiped;
import net.minecraft.RenderBiped;
import net.minecraft.RenderShadow;
import net.minecraft.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(RenderShadow.class)
public abstract class RenderShadowMixin extends RenderBiped {
    public RenderShadowMixin(ModelBiped par1ModelBiped, float par2) {
        super(par1ModelBiped, par2);
    }

    @Inject(method = "setTextures", at = @At("HEAD"), cancellable = true)
    private void setTextureWithEmission(CallbackInfo ci) {
        this.setTexture(0, "textures/entity/shadow");
        this.textures_glowing[0] = new ResourceLocation("textures/entity/shadow_glow.png");
        ci.cancel();
    }
}
