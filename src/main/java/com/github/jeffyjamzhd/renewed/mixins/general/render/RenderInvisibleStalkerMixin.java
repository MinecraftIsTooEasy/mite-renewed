package com.github.jeffyjamzhd.renewed.mixins.general.render;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(RenderInvisibleStalker.class)
public abstract class RenderInvisibleStalkerMixin extends RenderBiped {
    public RenderInvisibleStalkerMixin(ModelBiped par1ModelBiped, float par2) {
        super(par1ModelBiped, par2);
    }

    @ModifyReturnValue(method = "getModelOpacity", at = @At("RETURN"))
    private float setModelOpacity(float original, @Local(argsOnly = true) Entity entity) {
        EntityPlayer player = Minecraft.getClientPlayer();
        double dist = player.getDistanceSqToEntity(entity);
        return (float) Math.max(0.01F, Math.min(0.5F, (50 - dist) / 100));
    }
}
