package com.github.jeffyjamzhd.renewed.mixins.general.render;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(RenderInvisibleStalker.class)
public abstract class RenderInvisibleStalkerMixin extends RenderBiped {
    public RenderInvisibleStalkerMixin(ModelBiped par1ModelBiped, float par2) {
        super(par1ModelBiped, par2);
    }

    @Inject(method = "setTextures", at = @At("HEAD"), cancellable = true)
    private void setTextureWithEmission(CallbackInfo ci) {
        this.setTexture(0, "textures/entity/wight");
        this.textures_glowing[0] = new ResourceLocation("textures/entity/shadow_glow.png");
        ci.cancel();
    }

    @ModifyReturnValue(method = "getModelOpacity", at = @At("RETURN"))
    private float setModelOpacity(float original, @Local(argsOnly = true) Entity entity) {
        EntityPlayer player = Minecraft.getClientPlayer();
        double dist = player.getDistanceSqToEntity(entity);
        float opacity = (float) Math.max(0.05F, Math.min(0.5F, (50 - dist) / 100));
        return opacity;
    }
}
