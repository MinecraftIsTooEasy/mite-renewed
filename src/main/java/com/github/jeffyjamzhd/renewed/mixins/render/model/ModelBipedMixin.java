package com.github.jeffyjamzhd.renewed.mixins.render.model;

import com.github.jeffyjamzhd.renewed.item.ItemPolearm;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Will probably implement held item renderers in the future.
 */

@Mixin(ModelBiped.class)
public class ModelBipedMixin {
//    @Shadow public ModelRenderer bipedRightArm;
//    @Shadow public ModelRenderer bipedLeftArm;
//
//    @Inject(method = "setRotationAngles", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/MathHelper;sin(F)F", ordinal = 7))
//    private void setAnglesForPolearm(
//            float par1, float par2, float par3, float par4, float par5, float par6,
//            Entity entity, CallbackInfo ci) {
//        if (entity instanceof EntityLiving living) {
//            ItemStack held = living.getHeldItemStack();
//            if (held != null && held.getItem() instanceof ItemPolearm) {
//
//            }
//        }
//    }
}
