package com.github.jeffyjamzhd.renewed.mixins.compat.rustedironcore;

import com.bawnorton.mixinsquared.TargetHandler;
import com.github.jeffyjamzhd.renewed.api.IModelBiped;
import com.github.jeffyjamzhd.renewed.item.ItemPolearm;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Consumer;

@Mixin(value = RenderPlayer.class, priority = 2000)
public abstract class RenderPlayerRICMixin extends RendererLivingEntity {
    @Unique
    private List<ModelBiped> modelsCopy;

    public RenderPlayerRICMixin(ModelBase par1ModelBase, float par2) {
        super(par1ModelBase, par2);
    }

    @TargetHandler(
            mixin = "moddedmite.rustedironcore.mixin.client.render.RenderPlayerMixin",
            name = "func_130009_a"
    )
    @WrapOperation(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V", ordinal = 2), remap = false)
    private void injectPolearmCheck(
            List instance, Consumer consumer, Operation<Void> original, @Local(name = "var11") ItemStack stack) {
        // Copy RIC model list
        if (modelsCopy == null)
            this.modelsCopy = instance;

        // Set aim
        if (stack.getItem() instanceof ItemPolearm) {
            instance.forEach(x -> ((IModelBiped) x).mr$setAimingPolearm(true));
        } else {
            original.call(instance, consumer);
        }
    }

    @TargetHandler(
            mixin = "moddedmite.rustedironcore.mixin.client.render.RenderPlayerMixin",
            name = "func_130009_a"
    )
    @Inject(method = "@MixinSquared:Handler", at = @At("TAIL"))
    private void injectPolearmFalse(
            AbstractClientPlayer clientPlayer, double par2, double par4,
            double par6, float par8, float par9, CallbackInfo ci) {
        // Reset aim
        if (modelsCopy != null) {
            modelsCopy.forEach(modelBiped -> modelBiped.mr$setAimingPolearm(false));
        }
    }
}
