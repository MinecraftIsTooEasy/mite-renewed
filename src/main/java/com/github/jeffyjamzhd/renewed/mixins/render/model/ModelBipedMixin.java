package com.github.jeffyjamzhd.renewed.mixins.render.model;

import com.github.jeffyjamzhd.renewed.api.IModelBiped;
import com.github.jeffyjamzhd.renewed.render.ItemRenderPolearm;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Will probably implement held item renderers in the future.
 */

@Mixin(ModelBiped.class)
public class ModelBipedMixin implements IModelBiped {
    @Shadow public boolean aimedBow;
    @Shadow public ModelRenderer bipedRightArm;
    @Shadow public ModelRenderer bipedLeftArm;
    @Unique private boolean mr$aimedPolearm;

    @Override
    public boolean mr$isAimingPolearm() {
        return this.mr$aimedPolearm;
    }

    @Override
    public void mr$setAimingPolearm(boolean value) {
        mr$aimedPolearm = value;
    }

    @Inject(method = "setRotationAngles", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/MathHelper;sin(F)F", ordinal = 7), cancellable = true)
    private void specialUseAnimation(
            float par1, float par2, float par3,
            float par4, float par5, float par6,
            Entity entity, CallbackInfo ci
    ) {
        if (this.mr$aimedPolearm) {
            ItemRenderPolearm.handlePlayerArms(entity, bipedLeftArm, bipedRightArm, par3, par6);
            ci.cancel();
        }
    }
}
