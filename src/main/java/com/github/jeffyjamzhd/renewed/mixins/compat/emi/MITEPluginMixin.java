package com.github.jeffyjamzhd.renewed.mixins.compat.emi;

import com.llamalad7.mixinextras.sugar.Local;
import dev.emi.emi.api.EmiRegistry;
import moddedmite.emi.MITEPlugin;
import net.minecraft.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MITEPlugin.class)
public class MITEPluginMixin {
    @Inject(method = "register", at = @At(value = "INVOKE", target = "Lnet/minecraft/Item;getNutrition()I", ordinal = 0))
    private void checkRenewedFood(EmiRegistry registry, CallbackInfo ci, @Local Item item) {
    }
}
